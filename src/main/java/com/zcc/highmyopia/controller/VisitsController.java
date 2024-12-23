package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.mapper.IVisitsMapper;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.Visits;
import com.zcc.highmyopia.service.IVisitsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "患者就诊信息管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/visits")
public class VisitsController {

    private final IVisitsService visitsService;

    @GetMapping("find/{patientId}")
    @ApiOperation(value = "查找患者就诊信息")
    @RequiresAuthentication
    public Result findVisitsByPatientId(@PathVariable(name = "patientId") Long patientId){
        List<Visits> list = visitsService.list(new LambdaQueryWrapper<Visits>()
                .eq(Visits::getPatientId, patientId));
        return Result.succ(list);
    }
    @GetMapping("/page")
    @ApiOperation(value = "分页查询患者就诊信息")
    @RequiresAuthentication
    public Result getVisitsPage(@RequestParam(defaultValue = "1") int pageNumber,
                                @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String diagName){
        return visitsService.getVisitsPage(pageNumber,pageSize, diagName);
    }

//    @GetMapping("/search")
//    @ApiOperation(value = "模糊查询患者就诊信息")
//    @RequiresAuthentication
//    public Result SearchVisits(){
//
//    }
}
