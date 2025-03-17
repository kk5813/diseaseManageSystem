package com.zcc.highmyopia.hospital.task;

import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.hospital.service.IGetDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    private final IGetDataService getDataService;

    @Scheduled(cron = "0 20 0 * * ?")
    // 执行内容
    public void exec() throws Exception {
        log.info("定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中");
        //getDataService.getTodayData();

        getDataService.getDataToday();
    }
}
