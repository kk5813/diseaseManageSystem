package com.zcc.highmyopia.hospital.task;

import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.repository.impl.SaveRepository;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            // 下载门诊处方信息
            downLoadService.getRecipe(dataNoSplit, dataNoSplit);

            // 下载患者就诊信息
            List<VisitEntity> patientVisits = downLoadService.getPatientVisit(dataNoSplit, dataNoSplit);
            if (patientVisits == null || patientVisits.isEmpty()) {
                log.warn("未获取到患者就诊信息");
                return;
            }
            List<String> visitNumbers = patientVisits.stream()
                    .map(VisitEntity::getVisitNumber)
                    .collect(Collectors.toList());

            List<String> patientIds = patientVisits.stream()
                    .map(patientVisit -> String.valueOf(patientVisit.getPatientId()))
                            .collect(Collectors.toList());
            // 下载检查报告信息
            downLoadService.getCheckResult(dataSplit, dataSplit, visitNumbers);
            // 下载门诊病历信息
            downLoadService.getOutElementByCondition(dataSplit, dataSplit, visitNumbers);
            // 下载检验结果
            downLoadService.getReportDetail(dataSplit, dataSplit, visitNumbers);
            // 下载视力眼压
            downLoadService.getElementVision(dataSplit, dataSplit, patientIds);

            // 下载图片到本地位置
            downLoadService.DownLoadReportImageBatch();
            log.info("每日定時任務完成");
        } catch (Exception e) {
            log.error("定时任务执行失败，错误信息: {}", e.getMessage(), e);
            throw new AppException(500, "定时任务执行失败: " + e.getMessage());
        }
    }
}
