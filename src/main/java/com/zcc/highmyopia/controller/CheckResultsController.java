package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.CheckResults;
import com.zcc.highmyopia.po.Visits;
import com.zcc.highmyopia.service.ICheckResultsService;
import com.zcc.highmyopia.service.IVisitsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/${app.config.api-version}/check_result")
public class CheckResultsController {

    private final ICheckResultsService checkResultsService;

    @GetMapping("find/{patientId}")
    @RequiresAuthentication
    public Result findVisitsByPatientId(@PathVariable(name = "patientId") Long patientId){
        List<CheckResults> list = checkResultsService.list(new LambdaQueryWrapper<CheckResults>()
                .eq(CheckResults::getPatientId, patientId));
        return Result.succ(list);
    }

}
