package com.zcc.highmyopia.AIDiagnose.manager.networkOperations.Impl;

import com.zcc.highmyopia.AIDiagnose.manager.networkOperations.ThirdApiClient;
import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import com.zcc.highmyopia.hospital.service.IDownLoadDataUtils;
import com.zcc.highmyopia.hospital.service.impl.DataDownloaderProxy;
import com.zcc.highmyopia.po.CheckReports;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ThirdApiClientImpl
 * @Description
 * @Author aigao
 * @Date 2026/1/17 22:15
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ThirdApiClientImpl implements ThirdApiClient {

    // 这里延续老代码的实现
    private IDownLoadDataUtils downLoadDataUtils;

    private final DataDownloaderProxy dataDownloaderProxy;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    @PostConstruct
    public void init() {
        downLoadDataUtils = dataDownloaderProxy.createProxy();
    }

    private static final List<CheckReportsEntity> EMPTY_CHECK_REPORTS = new ArrayList<>();

    @Override
    public List<CheckReportsEntity> getCheckReportsDataByHttp(String visitNumber, int addDate) throws Exception {
        String date = parseVisitNumberToDate(visitNumber);
        LocalDate localDate = LocalDate.parse(date, formatter);
        if(localDate.plusDays(1).getMonth() != localDate.getMonth()){
            // ### 奇怪的代码，必然是因为医院获取数据的接口相当奇怪，是的，他们就是简单的判断月份是否相同。😆
            log.warn("跨月获取检查报告数据，visitNumber={}, date={}", visitNumber, date);
            return EMPTY_CHECK_REPORTS;
        }
        // 往后addDate天，闭区间
        LocalDate rightDays = localDate.plusDays(addDate + 1);
        if(rightDays.getMonth() != localDate.getMonth()){
            // 这种就只能拿月末的了，不然就跨月了🤣
           rightDays = localDate.with(TemporalAdjusters.lastDayOfMonth());
        }
        return downLoadDataUtils.getCheckReportByVisitNumberNew(date, rightDays.format(formatter), visitNumber);
    }


    // 这里参数校验在Controller层就完成了，这里失败返回当天日期，纯粹是防御性编程
    public static String parseVisitNumberToDate(String visitNumber) {
        try {
            if (visitNumber == null || visitNumber.length() < 10) {
                // 如果 visitNumber 为空或长度不足10，返回当天日期
                return LocalDateTime.now().format(formatter);
            }
            // 提取 visitNumber 中的日期部分（假设格式为 XXyyyyMMdd...）
            String datePart = visitNumber.substring(2, 10); // 提取从索引2开始的8位字符
            System.out.println("datePart = " + datePart);
            LocalDate.parse(datePart,formatter);
            return datePart;
        } catch (Exception e) {
            // 如果解析失败或格式不正确，返回当天日期
            return LocalDateTime.now().format(formatter);
        }
    }
}
