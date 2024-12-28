package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.mapper.IVisitsMapper;
import com.zcc.highmyopia.po.*;
import com.zcc.highmyopia.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final IPatientsService patientsService;
    private final IDeptService deptService;
    private final IDoctorService doctorService;
    private final ISiteService siteService;

    @GetMapping("find/{patientId}")
    @ApiOperation(value = "查找患者就诊信息")
    @RequiresAuthentication
    public Result findVisitsByPatientId(@PathVariable(name = "patientId") Long patientId){
        List<Visits> visits = visitsService.list(new LambdaQueryWrapper<Visits>()
                .eq(Visits::getPatientId, patientId));
        List<VisitEntity> visitEntities = visits.stream()
                .map( visit -> {
                    Patients patient = patientsService.getPatientById(visit.getPatientId());
                    Dept dept = deptService.getDeptById(visit.getDeptId());
                    Doctor doctor = doctorService.getDoctorById(visit.getDoctorId());
                    Site site = siteService.getSiteById(visit.getSiteId());
                    return VisitEntity.poToEntity(visit, patient, doctor, dept, site);
                })
                .collect(Collectors.toList());
        return Result.succ(visitEntities);
    }
    @GetMapping("/page")
    @ApiOperation(value = "分页查询患者就诊信息")
    @RequiresAuthentication
    public Result getVisitsPage(@RequestParam(defaultValue = "1") int pageNumber,
                                @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String diagName){
        log.info("分页查询接口调用");
        IPage<Visits> visitsPage = visitsService.getVisitsPage(pageNumber, pageSize, diagName);
        List<Visits> records = visitsPage.getRecords();
        List<VisitEntity> visitEntities = records.stream()
                .map( visit -> {
                    Patients patient = patientsService.getPatientById(visit.getPatientId());
                    Dept dept = deptService.getDeptById(visit.getDeptId());
                    Doctor doctor = doctorService.getDoctorById(visit.getDoctorId());
                    Site site = siteService.getSiteById(visit.getSiteId());
                    if (patient == null || dept == null || doctor == null || site == null)
                        throw new AppException(401, "信息未找到");
                    return VisitEntity.poToEntity(visit, patient, doctor, dept, site);
                })
                .collect(Collectors.toList());
        // record对象
        IPage<VisitEntity> visitEntityIPage = new Page<>();

        // 设置新的分页信息
        visitEntityIPage.setRecords(visitEntities);
        visitEntityIPage.setTotal(visitsPage.getTotal());
        visitEntityIPage.setPages(visitsPage.getPages());
        visitEntityIPage.setCurrent(visitsPage.getCurrent());
        visitEntityIPage.setSize(visitsPage.getSize());
        return Result.succ(visitEntityIPage);
    }

//    @GetMapping("/search")
//    @ApiOperation(value = "模糊查询患者就诊信息")
//    @RequiresAuthentication
//    public Result SearchVisits(){
//
//    }
}
