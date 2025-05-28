package com.zcc.highmyopia.AI.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.AI.repository.IDiagnoseRepository;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;
import com.zcc.highmyopia.AI.service.tree.factory.engine.impl.DecisionTreeEngine;
import com.zcc.highmyopia.AI.service.tree.impl.LogicTreeNode;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import com.zcc.highmyopia.hospital.repository.ISaveToDataBase;
import com.zcc.highmyopia.hospital.service.IDownLoadDataUtils;
import com.zcc.highmyopia.hospital.service.impl.DataDownloaderProxy;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.ICheckReportsService;
import com.zcc.highmyopia.service.IReportFilesService;
import com.zcc.highmyopia.util.ThrowUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Slf4j
@Service
public class DiagnoseService implements IDiagnoseService {

    @Resource
    private IDiagnoseRepository diagnoseRepository;

    @Resource
    private DefaultTreeFactory treeFactory;

    private IDownLoadDataUtils downLoadDataUtils;
    @Resource
    private ICheckReportsService checkReportsService;

    @Resource
    private IReportFilesService reportFilesService;

    @Resource
    private  ISaveToDataBase saveToDataBase;
    @Resource
    private  DataDownloaderProxy dataDownloaderProxy;
    DateTimeFormatter formatterWithSplit = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter formatterNoSplit = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Value("${hospital.flask_path}")
    private String flaskPath;
    @PostConstruct
    public void init() {
        downLoadDataUtils = dataDownloaderProxy.createProxy();
    }
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public List<List<DiagnoseResultEntity>> diagnose(DiagnoseEntity diagnoseEntity, String isNeedDownLoad) throws Exception {
        String visitNumber = diagnoseEntity.getVisitNumber();
        ThrowUtils.throwIf(StringUtils.isBlank(visitNumber), ResultCode.VISIT_NUMBER_IS_NULL);
        // todo 这里只是简单的查询了visitNumber 在报告中是否出现，没有判断是否已经下载，一方面report_file表过于大，sql时间会很长，另一方面即使判断下载也可能出现文件不存在的情况。意义不大
        // List<CheckReports> checkReports =  diagnoseRepository.getCheckReportByVisitNumber(visitNumber);
        List<CheckReports> checkReports = null;
        // 一方面可以防止多次下载，一方面也能特定下载
        if("false".equals(isNeedDownLoad)){
            LambdaQueryWrapper<CheckReports> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(CheckReports::getVisitNumber,visitNumber);
            checkReports = checkReportsService.list(queryWrapper);
        }
        // 如果数据库中没有找到，则去下载, 添加一个如果对应的报告都没有被下载，则去下载。
        if(checkReports == null || checkReports.isEmpty() ||
                diagnoseRepository.getDownLoadReportFileCountByVisitNumber(visitNumber) <= 0){
            String curdataSplit = DiagnoseService.parseVisitNumber(visitNumber);
            List<CheckReportsEntity> checkReportsEntities = downLoadDataUtils.getCheckReportByVisitNumberNew(curdataSplit, curdataSplit, visitNumber);
            checkReports = saveToDataBase.saveCheckReportsByVisitNumber(visitNumber, checkReportsEntities);
            try{
                downLoadDataUtils.DownLoadReportImageBatchByVisitNumber(visitNumber);
            }catch (Exception e){
                log.error("此刻下载批量导入图片到本地发生异常",e);
                throw new BusinessException(ResultCode.Z_DownLoadReportImageBatchByVisitNumber);
            }
        }
        // 后续更多操作
        if(checkReports == null || checkReports.isEmpty() ){
            log.error("数据为空");
            throw new BusinessException(ResultCode.VISIR_NUMBER_NODATA_AFTER_DOWNLOAD);
        }
        log.info("基本数据已获取到");
        // 暂时多项相同的检查换成第一个
        List<CheckReports> values = new ArrayList<>(checkReports.stream()
                .collect(Collectors.toMap(CheckReports::getItemName, checkReport -> checkReport, (existing, replacement) -> existing))
                .values());
        RuleTreeVO ruleTreeVO = diagnoseRepository.getRuleTreeVOByDiseaseId(diagnoseEntity.getDiseaseId());
        //  光学相干断层成像（OCT）,扫描激光眼底检查(SLO)
        String input = ruleTreeVO.getInput();
        List<CheckReports> collect = values.stream().filter(e -> input.contains(e.getItemName())).collect(Collectors.toList());
        if (collect.size() != input.split(Constants.SPLIT).length)
            throw new AppException(400, "用户检查项目不够1");
        Map<String, String> url = new HashMap<>();
        collect.forEach(e -> {
            List<ReportFiles> reportFiles = diagnoseRepository.getReportFile(e.getId());
            if (reportFiles == null || reportFiles.isEmpty()) throw new AppException(400, "用户检查项目不够2");
            // todo 多个文件如何选择，暂时选择第一个
            ReportFiles reportFiles1 = reportFiles.get(0);
            String itemName = e.getItemName();
            url.put(itemName, reportFiles1.getFilePath());
        });
        // 现在只写一个SLO
        String filePath = url.get("扫描激光眼底检查(SLO)");
        // 修复路径
        filePath = filePath.startsWith("\\") ? filePath.substring(1) : filePath;
        filePath = filePath.replace("\\", "/");
        // log.info(filePath);
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            transactionTemplate.execute(status-> {
                log.info("Starting to delete data for visit number: {}", visitNumber);

                LambdaQueryWrapper<CheckReports> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(CheckReports::getVisitNumber, visitNumber);

                List<Object> reportIds = checkReportsService.list(queryWrapper).stream().map(CheckReports::getId
                ).collect(Collectors.toList());

                log.info("Found {} report IDs for visit number: {}", reportIds.size(), visitNumber);

                if (!reportIds.isEmpty()) {
                    LambdaQueryWrapper<ReportFiles> reportFilesLambdaQueryWrapper = Wrappers.lambdaQuery();
                    reportFilesLambdaQueryWrapper.in(ReportFiles::getReportId, reportIds);
                    reportFilesService.remove(reportFilesLambdaQueryWrapper);
                    log.info("Deleted report files for visit number: {}", visitNumber);
                }
                checkReportsService.remove(queryWrapper);
                log.info("Deleted check reports for visit number: {}", visitNumber);
                return null;
            });
            throw new BusinessException(400,"本地图片未找到，请重新下载！");
        }
        // 发送HTTP请求
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("imagePath", filePath);
        jsonMap.put("visitNumber", diagnoseEntity.getVisitNumber());
        String siteUrl = flaskPath + "/api/site";
        String body = LogicTreeNode.HttpPOST(siteUrl, jsonMap);
        List<DiagnoseResultEntity> lists = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                DiagnoseResultEntity.class);
        List<List<DiagnoseResultEntity>> res = new ArrayList<>();
        for (DiagnoseResultEntity list : lists) {
            Map<String, String> OneSiteUrl = new HashMap<>();
            OneSiteUrl.put("扫描激光眼底检查(SLO)", list.getUrl());
            DecisionTreeEngine decisionTreeEngine = treeFactory.openLogicTree(ruleTreeVO);
            List<DiagnoseResultEntity> process = new ArrayList<>();
            process.add(list);
            process.addAll(decisionTreeEngine.process(OneSiteUrl, diagnoseEntity.getVisitNumber()));
            res.add(process);
        }
        return res;
    }

    public static String parseVisitNumber(String visitNumber) {
        try {
            if (visitNumber == null || visitNumber.length() < 10) {
                // 如果 visitNumber 为空或长度不足10，返回当天日期
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
            // 提取 visitNumber 中的日期部分（假设格式为 XXyyyyMMdd...）
            String datePart = visitNumber.substring(2, 10); // 提取从索引2开始的8位字符
            System.out.println("datePart = " + datePart);
            LocalDate.parse(datePart,formatterNoSplit);
            return datePart;
        } catch (Exception e) {
            // 如果解析失败或格式不正确，返回当天日期
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

    public static void main(String[] args) {
        String s = parseVisitNumber("MZ202503230147");
        System.out.println("s = " + s);
    }
}
