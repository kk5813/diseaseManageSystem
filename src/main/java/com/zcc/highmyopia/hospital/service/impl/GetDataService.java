package com.zcc.highmyopia.hospital.service.impl;

import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.hospital.service.IGetDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/31
 * @Description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetDataService implements IGetDataService {


    DateTimeFormatter formatterWithSplit = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterNoSplit = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final IDownLoadService downLoadService;
    private final ISaveRepository repository;


    // 获取前一天的日期和时间
    LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
    String dataSplit = yesterday.format(formatterWithSplit);
    String dataNoSplit = yesterday.format(formatterNoSplit);



    @Override
    public void getTodayData() {
        log.info("定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中");
        try {
            // 1. 下载所有的当天就诊信息
            List<VisitEntity> visits = downLoadService.getPatientVisit(dataNoSplit, dataNoSplit);
            if (visits == null || visits.isEmpty()) {
                log.warn("未获取到当天的就诊信息");
                return;
            }
            List<String> visitNumbersList = visits.stream()
                    .map(VisitEntity::getVisitNumber).distinct().collect(Collectors.toList());
            List<String> patientIdsList = visits.stream()
                    .map(patientVisit -> String.valueOf(patientVisit.getPatientId())).distinct().collect(Collectors.toList());

            // 2.下载病人信息
            downLoadService.getPatientInfoByPatientId(patientIdsList);

            // 3. 获取当天的所有处方信息
            downLoadService.getRecipe(dataNoSplit, dataNoSplit);

            // 4.下载检查报告信息
            downLoadService.getCheckReportByPatientId(dataSplit, dataSplit, visitNumbersList);

            // 5.下载门诊病历信息
            downLoadService.getOutElementByVisitNumber(dataSplit, dataSplit, visitNumbersList);

            // 6.下载检验结果
            downLoadService.getReportDetail(dataSplit, dataSplit);

            // 7.下载视力眼压
            downLoadService.getElementVisionByVisitNumber(dataSplit, dataSplit, patientIdsList);

            // 8. 批量下载图片到本地位置
            downLoadService.DownLoadReportImageBatch();
            log.info("每日定時任務完成");
        } catch (Exception e) {
            log.error("定时任务执行失败，错误信息: {}", e.getMessage(), e);
            throw new AppException(500, "定时任务执行失败: " + e.getMessage());
        }
    }

    @Override
    public void getDataTest() {
        log.info("定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中");
        try {
            // 1. 下载所有的当天就诊信息
            List<VisitEntity> visits = downLoadService.getPatientVisit("20240603", "20240730");
            if (visits == null || visits.isEmpty()) {
                log.warn("未获取到当天的就诊信息");
                return;
            }
            List<String> visitNumbersList = visits.stream()
                    .map(VisitEntity::getVisitNumber).distinct().collect(Collectors.toList());
            List<String> patientIdsList = visits.stream()
                    .map(patientVisit -> String.valueOf(patientVisit.getPatientId())).distinct().collect(Collectors.toList());

            // 2.下载病人信息
            downLoadService.getPatientInfoByPatientId(patientIdsList);

            // 3. 获取当天的所有处方信息
            downLoadService.getRecipe("20240801", "20241125");

            // 4.下载检查报告信息
            downLoadService.getCheckReportByPatientId("20241219", "20241219", "1869575479859933185");

            // 5.下载门诊病历信息
            downLoadService.getOutElementByVisitNumber("2024-06-26", "20240725", "MZ202407071064");

            // 6.下载检验结果
            downLoadService.getReportDetail("2024-08-01", "2024-11-15");

            // 7.下载视力眼压
            downLoadService.getElementVisionByVisitNumber("20241219", "20241219", "1869575479859933185");

            // 8. 批量下载图片到本地位置
            downLoadService.DownLoadReportImageBatch();
            log.info("每日定時任務完成");
        } catch (Exception e) {
            log.error("定时任务执行失败，错误信息: {}", e.getMessage(), e);
            throw new AppException(500, "定时任务执行失败: " + e.getMessage());
        }
    }
}
