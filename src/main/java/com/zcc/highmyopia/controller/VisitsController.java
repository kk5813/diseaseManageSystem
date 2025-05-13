package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.dto.FollowAddsDTO;
import com.zcc.highmyopia.common.dto.PatientVisitsAddFollowUpDTO;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.po.*;
import com.zcc.highmyopia.service.*;
import com.zcc.highmyopia.util.OtherUtils;
import com.zcc.highmyopia.util.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
//    @GetMapping("/page")
//    @ApiOperation(value = "分页查询患者就诊信息")
//    @RequiresAuthentication
//    public Result getVisitsPage(@RequestParam(defaultValue = "1") int pageNumber,
//                                @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String diagName,
//                                @RequestParam(defaultValue = "") String startTime , @RequestParam(defaultValue = "") String endTime,
//                                @RequestParam(defaultValue = "") Long patientID){
//        log.info("分页查询接口调用");
//        IPage<Visits> visitsPage = visitsService.getVisitsPage(pageNumber, pageSize, diagName,startTime,endTime, patientID);
//        log.info("原分页查询正常");
//        List<Visits> records = visitsPage.getRecords();
//        List<VisitEntity> visitEntities = records.stream()
//                .map( visit -> {
//                    Patients patient = patientsService.getPatientById(visit.getPatientId());
//                    Dept dept = deptService.getDeptById(visit.getDeptId());
//                    Doctor doctor = doctorService.getDoctorById(visit.getDoctorId());
//                    Site site = siteService.getSiteById(visit.getSiteId());
//                    if (patient == null || dept == null || doctor == null || site == null)
//                        throw new AppException(401, "信息未找到");
//                    return VisitEntity.poToEntity(visit, patient, doctor, dept, site);
//                })
//                .collect(Collectors.toList());
//        // record对象
//        IPage<VisitEntity> visitEntityIPage = new Page<>();
//
//        // 设置新的分页信息
//        visitEntityIPage.setRecords(visitEntities);
//        visitEntityIPage.setTotal(visitsPage.getTotal());
//        visitEntityIPage.setPages(visitsPage.getPages());
//        visitEntityIPage.setCurrent(visitsPage.getCurrent());
//        visitEntityIPage.setSize(visitsPage.getSize());
//        return Result.succ(visitEntityIPage);
//    }

    @GetMapping("/page")
    @ApiOperation("分页查询患者就诊信息with科室名")
    @RequiresAuthentication
    public Result getVisitsByDeptName(@RequestParam(defaultValue = "1") int pageNumber,
                                      @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String diagName,
                                      @RequestParam(defaultValue = "") String startTime , @RequestParam(defaultValue = "") String endTime,
                                      @RequestParam(defaultValue = "") Long patientID,  @RequestParam(defaultValue = "") String deptName,
                                      @RequestParam(defaultValue = "") String doctorName
    ) {
        log.info("分页查询接口调用");
        IPage<Visits> visitsPage = visitsService.getVisitsPageWithDeptName(pageNumber, pageSize, diagName,startTime,endTime, patientID,deptName, doctorName);
        log.info("原分页查询正常");
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
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private IFollowupService followupService;

    @Resource
    private IRedisService redisService;

    @ApiOperation(value = "患者批量添加至随访")
    @PostMapping("/addFollowUpBatchByPatientsInfo")
    @RequiresAuthentication
    public Result addFollowUpBatchByPatientsInfo(@RequestBody List<FollowAddsDTO> followAddsDTOList){
        // todo 不建议用这个接口，因为1.用户操作可能会重复点击，导致数据重复添加，2.这种情况，不好实现幂等操作，小心被利用攻击导致内存爆炸。后面把这个接口注释了。
        final LocalDate now = LocalDate.now();
        List<Followup> collect = followAddsDTOList.stream().filter(x -> {
            LocalDate planTime = null;
            try {
                planTime = LocalDate.parse(x.getPlanVisitDate().trim(), dateTimeFormatter);
            } catch (Exception e) {
                return false;
            }
            // 保证随访日期一定是当前时刻之后
            return planTime.isAfter(now);
        }).map(x -> {
            Followup followup = FollowAddsDTO.dtoToPo(x);
            // 添加的一定是没有随访的
            followup.setVisitResult(Constants.FollowupStatus.NOT_FOLLOW);
            return followup;
        }).collect(Collectors.toList());
        boolean b = followupService.saveOrUpdateBatch(collect);
        return b ? Result.succ("操作成功") : Result.fail("操作失败");
    }
    @Autowired
    private IFollowupTemplateService followupTemplateService;

    @ApiOperation(value = "患者批量添加至随访,使用随访模板")
    @PostMapping("/addFollowUpBatchByTemplate")
    @RequiresAuthentication
    public Result addFollowUpBatchByTemplateWithPatients(@RequestBody PatientVisitsAddFollowUpDTO patientVisitsAddFollowUpDTO, HttpServletRequest request){
        String key = OtherUtils.GenerateRedisKey.generateKey(request, patientVisitsAddFollowUpDTO.generateHash());
        // 5s操作防抖
        boolean exist = redisService.isExistWithSet(key, "1", 1000 * 5);
        ThrowUtils.throwIf(exist, ResultCode.OPERATER_TOO_MUCH);

        final LocalDate now = LocalDate.now();
        String templateId = patientVisitsAddFollowUpDTO.getTemplateId();
        final FollowupTemplate template = followupTemplateService.getOne(new LambdaQueryWrapper<FollowupTemplate>().eq(FollowupTemplate::getId, templateId.trim()));
        ThrowUtils.throwIf(template == null, ResultCode.ID_NOT_FOUND, "没有与此ID想对应的随访模板");
        LocalDate planTime = now.plusDays(template.getIntervalValue());
        String planVisitDate = planTime.format(dateTimeFormatter);
        List<Followup> collect = patientVisitsAddFollowUpDTO.getInfos().stream()
                .map(info -> {
                    Followup followup = FollowupTemplate.TemplateToLocalFollowup(template);
                    followup.setDeptId(info.getDeptId());
                    followup.setDoctorId(info.getDoctorId());
                    followup.setPatientId(info.getPatientId());
                    followup.setVisitNumber(info.getVisitNumber());
                    followup.setPlanVisitDate(planVisitDate);
                    return followup;
                }).collect(Collectors.toList());
        boolean b = followupService.saveOrUpdateBatch(collect);
        return b ? Result.succ("操作成功") : Result.fail("操作失败");
    }
//    @GetMapping("/search")
//    @ApiOperation(value = "模糊查询患者就诊信息")
//    @RequiresAuthentication
//    public Result SearchVisits(){
//
//    }
}
