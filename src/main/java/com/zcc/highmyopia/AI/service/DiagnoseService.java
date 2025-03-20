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
import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import com.zcc.highmyopia.hospital.repository.ISaveToDataBase;
import com.zcc.highmyopia.hospital.service.IDownLoadDataUtils;
import com.zcc.highmyopia.hospital.service.impl.DataDownloaderProxy;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.IReportFilesService;
import com.zcc.highmyopia.util.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    private  ISaveToDataBase saveToDataBase;
    @Resource
    private  DataDownloaderProxy dataDownloaderProxy;
    DateTimeFormatter formatterWithSplit = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Value("${hospital.flask_path}")
    private String flaskPath;
    @PostConstruct
    public void init() {
        downLoadDataUtils = dataDownloaderProxy.createProxy();
    }

    @Override
    public List<List<DiagnoseResultEntity>> diagnose(DiagnoseEntity diagnoseEntity) throws Exception {
        String visitNumber = diagnoseEntity.getVisitNumber();
        // todo 这里只是简单的查询了visitNumber 在报告中是否出现，没有判断是否已经下载，一方面report_file表过于大，sql时间会很长，另一方面即使判断下载也可能出现文件不存在的情况。意义不大
        List<CheckReports> checkReports =  diagnoseRepository.getCheckReportByVisitNumber(visitNumber);

        // 如果数据库中没有找到，则去下载
        // todo 这里耦合了
        if(checkReports == null || checkReports.isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String curdataSplit = LocalDateTime.now().format(formatter);

            List<CheckReportsEntity> checkReportsEntities = downLoadDataUtils.getCheckReportByVisitNumberNew(curdataSplit, curdataSplit, visitNumber);
            checkReports = saveToDataBase.saveCheckReportsByVisitNumber(visitNumber, checkReportsEntities);
            try{
                downLoadDataUtils.DownLoadReportImageBatchByVisitNumber(visitNumber);
            }catch (Exception e){
                log.error("批量导入图片到本地发生异常",e);
                throw e;
            }
        }
        // 暂时多项相同的检查换成第一个
        List<CheckReports> values = new ArrayList<>(checkReports.stream()
                .collect(Collectors.toMap(CheckReports::getItemName, checkReport -> checkReport, (existing, replacement) -> existing))
                .values());
        RuleTreeVO ruleTreeVO = diagnoseRepository.getRuleTreeVOByDiseaseId(diagnoseEntity.getDiseaseId());
        //  光学相干断层成像（OCT）,扫描激光眼底检查(SLO)
        String input = ruleTreeVO.getInput();
        List<CheckReports> collect = values.stream().filter(e -> input.contains(e.getItemName())).collect(Collectors.toList());
        if (collect.size() != input.split(Constants.SPLIT).length)
            throw new AppException(400, "用户检查项目不够");
        Map<String, String> url = new HashMap<>();
        collect.forEach(e -> {
            List<ReportFiles> reportFiles = diagnoseRepository.getReportFile(e.getId());
            if (reportFiles == null || reportFiles.isEmpty()) throw new AppException(400, "用户检查项目不够");
            // todo 多个文件如何选择，暂时选择第一个
            ReportFiles reportFiles1 = reportFiles.get(0);
            String itemName = e.getItemName();
            url.put(itemName, reportFiles1.getFilePath());
        });
        // 现在只写一个SLO
        String filePath = url.get("扫描激光眼底检查(SLO)");
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
}
