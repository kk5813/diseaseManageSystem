package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * @Description 此方法为当天来的患者进行第三方库表查询
     * @param
     */
    @GetMapping("onlySearch/{patientId}")
    @ApiOperation(value = "当天来的患者进行第三方库表查询")
    @RequiresAuthentication
    public Result onlySearch(@PathVariable(name = "patientId") Long patientId){
        return Result.succ(null);
    }

}
