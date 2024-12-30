package com.zcc.highmyopia.hospital.task;

import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Component()
public class GetDataTask {

    DateTimeFormatter formatterWithSplit = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterNoSplit = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final IDownLoadService downLoadService;
    private final ISaveRepository repository;

    @Scheduled(cron = "0 0 0 * * ?")
    // 执行内容
    public void exec() throws Exception {
        log.info("定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中");

        // 获取前一天的日期和时间
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        String dataSplit = yesterday.format(formatterWithSplit);
        String dataNoSplit = yesterday.format(formatterNoSplit);

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
            downLoadService.getCheckResultByPatientId(dataSplit, dataSplit, visitNumbersList);

            // 5.下载门诊病历信息
            downLoadService.getOutElementByVisitNumber(dataSplit, dataSplit, visitNumbersList);

            // 6.下载检验结果
            downLoadService.getReportDetail(dataSplit, dataSplit, visitNumbersList);

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
}
