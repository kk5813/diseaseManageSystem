package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.dto.CategoryCountDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.CategoryGroupCountVO;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.hospital.service.IGetDataService;
import com.zcc.highmyopia.service.ICheckReportsService;
import com.zcc.highmyopia.service.IVisitsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private final IVisitsService visitsService;
    private final IGetDataService getDataService;

    /**
     * @Description 此方法为当天来的患者进行第三方库表查询
     * @param
     */
    @GetMapping("onlySearch/{patientId}")
    @ApiOperation(value = "当天来的患者进行第三方库表查询")
    @RequiresAuthentication
    public Result onlySearch(@PathVariable(name = "patientId") Long patientId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDateTime.now().format(formatter);
        downLoadService.getCheckReportByPatientId(today, today, String.valueOf(patientId));
        downLoadService.DownLoadReportImageBatch();
        return Result.succ(null);
    }

    @PostMapping("CategoryCount")
    @ApiOperation(value = "分门别类患病人数统计")
    @RequiresAuthentication
    public Result categoryCount(@RequestBody CategoryCountDTO categoryCountDTO){
        log.info("收到请求");
        List<CategoryGroupCountVO> categoryGroupCountVOList = visitsService.categoryCount(categoryCountDTO);
        return Result.succ(categoryGroupCountVOList);
    }

    @GetMapping("get_test_data")
    @ApiOperation(value = "部署服务器上拉取数据使用")
    @RequiresAuthentication
    public Result getTestData(){
        getDataService.getDataTest();
        return Result.succ(null);
    }

}
