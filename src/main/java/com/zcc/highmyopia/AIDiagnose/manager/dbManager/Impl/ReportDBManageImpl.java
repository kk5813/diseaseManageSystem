package com.zcc.highmyopia.AIDiagnose.manager.dbManager.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcc.highmyopia.AIDiagnose.manager.dbManager.ReportDBManage;
import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.repository.ISaveToDataBase;
import com.zcc.highmyopia.hospital.service.IDownLoadDataUtils;
import com.zcc.highmyopia.hospital.service.impl.DataDownloaderProxy;
import com.zcc.highmyopia.mapper.ICheckReportsMapper;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.ICheckReportsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ReportDBManageImpl
 * @Description
 * @Author aigao
 * @Date 2026/1/17 23:59
 * @Version 1.0
 */
@Component
@Slf4j
@AllArgsConstructor
public class ReportDBManageImpl implements ReportDBManage {
    private final ICheckReportsMapper checkReportsMapper;
    private final IReportFilesMapper reportFilesMapper;

    private IDownLoadDataUtils downLoadDataUtils;
    private final DataDownloaderProxy dataDownloaderProxy;

    private final ISaveToDataBase saveToDataBase;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final DateTimeFormatter checkTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @PostConstruct
    public void init() {
        downLoadDataUtils = dataDownloaderProxy.createProxy();
    }



    @Override
    public List<CheckReports> getCheckReportsDataByDB(String visitNumber) {
        return checkReportsMapper.getCheckReportByVisitNumber(visitNumber);
    }

    /**
     *  dbCheckReports 数据库中已经存在的检查报告记录， httpCheckReports三方接口中获取到的检查报告记录
     * @param httpCheckReports
     * @param dbCheckReports
     * @return
     *
     *    1. 先判断dbCheckReports中的项目是否齐全，不全补
     *    2. 再判断每个项目对应的报告文件是否存在，不全补
     *    3. 根据httpCheckReports补报告文件和重写数据库。
     */
    @Override
    public Map<String, List<ReportFiles>> ensureReportsReady(List<CheckReportsEntity> httpCheckReports, List<CheckReports> dbCheckReports) {
        // 1. 定义最终返回结果
        Map<String, List<ReportFiles>> resultMap = new HashMap<>();
        // 2. 构建dbCheckReports的Map，key为检查项目+检查日期，value为CheckReports
        // 这里认为checkTime相同和检查项目相同的是同一个检查项目
        Map<String, CheckReports> dbMap = dbCheckReports.stream().collect(Collectors.toMap(
                checkReports -> generateFingerKey(checkReports.getItemName(), checkReports.getCheckTime().format(checkTimeFormatter)),
                checkReports -> checkReports, (v1, v2) -> v1));
        // 3. 遍历httpCheckReports，进行补全
        for(CheckReportsEntity checkReportsEntity:httpCheckReports){
            // 3.1 以httpCheckReports的检查项目+检查日期为key,去dbCheckReports的Map中查找
            List<ReportFiles> readyFiles = null;
            String fingerKey = generateFingerKey(checkReportsEntity.getItemName(), checkReportsEntity.getCheckTime());
            CheckReports checkReports = dbMap.getOrDefault(fingerKey, null);
            // 3.2 判断检查项目在数据库中是否存在，报告文件是否存在，如果不存在，则新增CheckReports记录，并下载报告文件，保存到数据库
            Pair pair = shouldDownLoad(checkReports);
            if(pair.needDownLoad){
                // 先将要下载的数据项存数据库，然后再下载并更新状态。 todo 这里两个事务可以考虑合并
                List<ReportFiles> reportFiles = saveToDataBase.saveCheckReportsByEntity(checkReportsEntity);
                downLoadDataUtils.processReportFileDownload(reportFiles);
                readyFiles = reportFiles;
            } else {
                // 数据库中已经存在，且文件齐全，直接获取文件列表
                readyFiles = pair.reportFiles;
            }
            // 这里就会把ItemName相同的文件都放到一个list中，包括不同checkTime
            resultMap.computeIfAbsent(checkReportsEntity.getItemName(), k -> new ArrayList<>()).addAll(readyFiles);
        }
        return resultMap;
    }

    private String generateFingerKey(String itemName, String checkTime){
        return (itemName + "_" + checkTime).trim();
    }

    private final static Pair TruePair = new Pair(true, new ArrayList<>());
    @Data
    @AllArgsConstructor
    static class Pair{
        boolean needDownLoad;
        List<ReportFiles> reportFiles;
    }
    private Pair shouldDownLoad(CheckReports checkReports){
        // 先判断checkReports是否为空，或者visitNumber为空
        if(checkReports == null || StringUtils.isBlank(checkReports.getVisitNumber()))  {return TruePair;}
        // 获取这些数据对应的文件列表
        List<ReportFiles> reportFiles = reportFilesMapper.getReportFilesByVisitNumberAndNameAndCheckTime(
                checkReports.getVisitNumber(),
                checkReports.getItemName(),
                checkReports.getCheckTime().format(checkTimeFormatter)
        );
        // 数据库中有记录，但没有关联的文件，必须下载
        if(reportFiles == null || reportFiles.isEmpty()) {return TruePair;}
        // 判断每个文件是否真实存在
        for(ReportFiles reportFile: reportFiles){
            log.warn("本地文件丢失，触发重新同步: reportId={}, path={}", checkReports.getId(), reportFile.getFilePath());
            if(reportFile.getFilePath() == null || !new File(reportFile.getFilePath()).exists()){
                return TruePair;
            }
        }
        // 此时认为文件数据库和本地路径都存在
        return new Pair(false, reportFiles);
    }
}
