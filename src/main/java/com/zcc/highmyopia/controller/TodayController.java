package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.hospital.service.AsyncDownLoadService;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.service.ICheckReportsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "当天患者查询管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/today")
public class TodayController {


    private final ICheckReportsService checkReportsService;
    private final IDownLoadService downLoadService;
    private final AsyncDownLoadService asyncDownLoadService;

    /**
     * @Description 此方法为当天来的患者进行第三方库表查询
     * @param
     */
    @GetMapping("onlySearch/{patientId}")
    @ApiOperation(value = "当天来的患者进行第三方库表查询")
    @RequiresAuthentication
    public Result onlySearch(@PathVariable(name = "patientId") Long patientId){
        // 根据patientId查CheckReport
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDateTime.now().format(formatter);
        // 1. 根据patientId查到这个人的CheckReport和FileUrl
        downLoadService.getCheckResult(today, today, String.valueOf(patientId));
        // 2. 下载数据库中未下载的图像并保存到本地
        downLoadService.DownLoadReportImageBatch();

        // 3. 异步先将原始图像的Path返回给前端

        // 4. 调用深度学习模型诊断的服务并得到结果再次返回给前端数据

        return Result.succ(null);
    }


    @GetMapping("test")
    @ApiOperation(value = "当天来的患者进行第三方库表查询")
    @RequiresAuthentication
    public Result testAsync() {
        // 4. 异步执行图像下载和深度学习模型诊断
        CompletableFuture<String> downloadTask = asyncDownLoadService.downloadReportImage();
        CompletableFuture<String> modelTask = asyncDownLoadService.callDeepLearningModel();

        // 不等待任务完成，立即返回响应
        return Result.succ("Tasks have been started, you can query later for the results.");
    }



}
