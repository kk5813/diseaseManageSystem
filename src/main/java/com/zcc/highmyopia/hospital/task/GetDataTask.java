package com.zcc.highmyopia.hospital.task;

import com.zcc.highmyopia.hospital.service.IDownLoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Component()
public class GetDataTask {

    private final IDownLoadService dataService;

    @Scheduled(cron = "0 0 0 * * ?")
    // 执行内容
    public void exec(){
        log.info("定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中");

    }
}
