package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.hospital.service.IGetDataService;
import com.zcc.highmyopia.hospital.utils.State;
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
 * @Date 2025/5/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "调试接口")
@RestController
@RequestMapping("/api/${app.config.api-version}/test")
public class TestController {


    private final IGetDataService getDataService;

    @GetMapping("/downLoad")
    @ApiOperation(value = "夜间下载测试接口")
    @RequiresAuthentication
    public Result testDownLoad(){
        try {
            State dataToday = getDataService.getDataToday();
            return Result.succ(dataToday.getState());
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(null);
        }
    }
}
