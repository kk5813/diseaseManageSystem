package com.zcc.highmyopia.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.dto.FollowupBatchDTO;
import com.zcc.highmyopia.common.dto.FollowupDTO;
import com.zcc.highmyopia.common.vo.FollowupPatientVO;
import com.zcc.highmyopia.common.vo.ListFollowupVO;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.po.Followup;
import com.zcc.highmyopia.mapper.IFollowupMapper;
import com.zcc.highmyopia.service.IFollowupService;
import com.zcc.highmyopia.service.impl.FollowupService;
import com.zcc.highmyopia.util.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangyue
 * @since 2021-03-04
 */
@RestController
@Api(tags = "随访管理")
@RequestMapping("/api/${app.config.api-version}/followup")
public class FollowupController {

    @Autowired
    IFollowupService followupService;
    @Autowired
    IFollowupMapper followupMapper;

//    /**
//     * 今日未随访
//     * @return
//     */
//    @GetMapping("/todayUndo")
//    @ApiOperation(value = "今日未随访")
//    @RequiresAuthentication
//    public Result todayUndoFollowup() {
//        return Result.succ(followupMapper.selectToDayFollowUpList());
//    }
//
//    /**
//     *
//     * 超期未随访
//     * @return
//     */
//    @GetMapping("/overdue")
//    @ApiOperation(value = "超期未随访")
//    @RequiresAuthentication
//    public Result overdueFollowup() {
//        return Result.succ(followupMapper.selectOverdueFollowUpList());
//    }
//
//    /**
//     * 全部未随访
//     * @return
//     */
//    @GetMapping("/undo")
//    @RequiresAuthentication
//    @ApiOperation(value = "全部未随访")
//    public Result undoFollowup() {
//        return Result.succ(followupMapper.selectUndoFollowUpList());
//    }

    @GetMapping("/search")
    @RequiresAuthentication
    @ApiOperation(value = "模糊查询")
    public Result searchFollowup(@RequestParam(defaultValue = "") String patientId, @RequestParam(defaultValue = "") String dateStart,
                                 @RequestParam(defaultValue = "") String dateEnd, @RequestParam(defaultValue = "") Integer visitResult,
                                  @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
                                 @RequestParam(defaultValue = "") String deptName, @RequestParam(defaultValue = "") String doctorName,
                                 @RequestParam(defaultValue = "") String visitNumber
    ){
        //followupService.SearchFollowup(patientId,dateStart,dateEnd,visitResult,pageNo,pageSize);
        // 这里的visitNumber 输入为MZ就是门诊了
        return Result.succ(followupService. SearchFollowPatient(pageNo,pageSize,patientId, visitNumber, visitResult, dateStart, dateEnd, doctorName,deptName));
    }
    private String addParamsInfo =  "添加随访需要6项参数：{PatientId,PlanVisitDate,VisitPlan,DeptId,DoctorId,visitNumber}";

    @PostMapping("/addFollowup")
    @ApiOperation(value = "添加随访记录")
    @RequiresAuthentication
    public Result addFollowup(@RequestBody FollowupDTO followupDTO) {
        ThrowUtils.throwIf(StringUtils.isBlank(followupDTO.getPatientId()), new BusinessException(ResultCode.ID_FIELD_MISSING, "缺少病人ID，添加随访需要6项参数：" + addParamsInfo));
        ThrowUtils.throwIf(StringUtils.isBlank(followupDTO.getPlanVisitDate()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺少计划随访时间PlanVisitDate，"+ addParamsInfo));
        ThrowUtils.throwIf(StringUtils.isBlank(followupDTO.getVisitPlan()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺失visitPlan!，"+ addParamsInfo));
        ThrowUtils.throwIf(StringUtils.isBlank(followupDTO.getDeptId()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺少deptId部门ID,"+ addParamsInfo));
        ThrowUtils.throwIf(StringUtils.isBlank(followupDTO.getDoctorId()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺少doctorId医生ID, "+ addParamsInfo));
        ThrowUtils.throwIf(StringUtils.isBlank(followupDTO.getVisitNumber()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺少visitNumber就诊号，"+ addParamsInfo));
        if(StringUtils.isBlank(followupDTO.getVisitDate())){
            followupDTO.setVisitDate(null);
        }
        Followup followup = FollowupDTO.dtoToPo(followupDTO);
        followup.setVisitResult(0);
        followup.setCreateTime(LocalDateTime.now());
        followup.setUpdateTime(LocalDateTime.now());
        followupService.saveOrUpdate(followup);
        return Result.succ(null);
    }

    /**
     * 批量设置已随访，
     * 哪些要设置，为前端进行刷选，后端只设置状态
     * 随访状态：已随访还要随访这种，这个接口不实现，如果有需求，单独写一个，因为还要随访的这种需要的参数比较多，而且可以从病历那个模板处添加
     * 故这里只实现状态设置
    * */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/setFollowupBatch")
    @ApiOperation(value = "批量设置已随访")
    @RequiresAuthentication
    public Result setFollowupBatch(@RequestBody FollowupBatchDTO followupBatchDTO){
        if(followupBatchDTO.getFollowupIds() == null || followupBatchDTO.getFollowupIds().isEmpty()){
            return Result.succ("操作成功", null);
        }
        String visitDate = followupBatchDTO.getVisitDate();
        if(StringUtils.isBlank(visitDate)){
            visitDate = LocalDate.now().format(formatter);
        }
        LocalDate visitDataTime = null;
        try{
            visitDataTime = LocalDate.parse(visitDate.trim(), formatter);
        }catch (DateTimeParseException e){
            throw new BusinessException(ResultCode.DATE_VARIABLE_ERROR);
        }
        final LocalDate finalVisitDataTime = visitDataTime;

        LambdaQueryWrapper<Followup> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.in(Followup::getId, followupBatchDTO.getFollowupIds());
        List<Followup> list = followupService.list(lambdaQueryWrapper);
        List<Followup> errorList = new ArrayList<>();
        List<Followup> collect = list.stream().filter(followup -> {
            if(followup == null) {
                return false;
            }
            LocalDate planTime = null;
            try {
                planTime = LocalDate.parse(followup.getPlanVisitDate().substring(0, 10), formatter);
            } catch (DateTimeParseException e) {
                throw new BusinessException(ResultCode.DATE_VARIABLE_ERROR);
            }
            // 到访时间不早于计划时间
            if (finalVisitDataTime.isBefore(planTime)) {
                errorList.add(followup);
                return false;
            }
            return true;
        }).peek(followup -> {
            followup.setVisitResult(Constants.FollowupStatus.FOLLOW_SUCCESS);
        }).collect(Collectors.toList());
        followupService.saveOrUpdateBatch(collect);
        if(!errorList.isEmpty()){
            return Result.fail("部分随访修改失败，原因设置的到访时间早于了计划时间", errorList);
        }
        return Result.succ(null);
    }


    /**
     *
     * 修改随访记录
     * @param listFollowup
     * @return
     */
    @PostMapping("/editFollowup")
    @ApiOperation(value = "修改随访记录")
    @RequiresAuthentication
    public Result editFollowup(@RequestBody FollowupDTO followupDTO) {
        /**
         * 理一下业务，这个时随访记录修改，1.首先目前所有记录均可修改，包括随访成功的。2.必须存在记录
         * 3.对于随访成功的不应该修改到访时间 4.对于未随访，可以在没有来的时候修改随访信息（也就是存在不修改到访时间的情况，前端保证逻辑，后端只校验）
         * 5. 对于需要继续随访的，应该采用添加而非修改的方式，因为要存以前的记录。
         * 6. 必传参数，Id, VisitResult，PlanVisitDate，PatientId
         * */
        Followup followup = followupService.getById(followupDTO.getId());
        ThrowUtils.throwIf(followup==null || followup.getVisitResult() == -1, ResultCode.ID_NOT_FOUND);
        // Todo 暂定visit_result 0 为待随访 ， 等于1为随访成功， -1为记录删除，
        if(followupDTO.getVisitResult() == Constants.FollowupStatus.DELETE){
            // 删除记录
            followup.setVisitResult(followupDTO.getVisitResult());
            followupService.saveOrUpdate(followup);
            return Result.succ("删除成功！");
        }

        // 没有访问，可以修改成其他状态，0 ，1， 2，
        if(followup.getVisitResult() == Constants.FollowupStatus.NOT_FOLLOW) {
            ThrowUtils.throwIf(StringUtils.isBlank(followupDTO.getPlanVisitDate()),new BusinessException(ResultCode.PARAMS_FIELD_MISSING));
            // 计划随访时间不可能 比今天早，修改计划随访时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate planTime = null;
            try{
                planTime = LocalDate.parse(followupDTO.getPlanVisitDate().trim(), formatter);
            }catch (DateTimeParseException e){
                throw new BusinessException(ResultCode.DATE_VARIABLE_ERROR, "Invalid start date format");
            }
            ThrowUtils.throwIf(planTime.isBefore(LocalDate.now()), new BusinessException(ResultCode.DATE_VARIABLE_ERROR));
            followup.setPlanVisitDate(planTime.toString());

            //Todo  这里能修改病人id是不是有点逻辑问题
            // followup.setPatientId(followupDTO.getPatientId());
            followup.setVisitResult(followupDTO.getVisitResult());
            if(followupDTO.getVisitResult() == Constants.FollowupStatus.FOLLOW_SUCCESS){
                // 成功到访，记录到访时间，否则不更新时间，存在只修改记录而不到访的情况
                followup.setVisitDate(LocalDateTime.now().toString());
            }else{
                followup.setVisitDate(null);
            }
        }
        if(StringUtils.isNotBlank(followupDTO.getVisitPlan())){
            followup.setVisitPlan(followupDTO.getVisitPlan());
        }
        if(StringUtils.isNotBlank(followupDTO.getVisitContent())){
            followup.setVisitContent(followupDTO.getVisitContent());
        }
        if(StringUtils.isNotBlank(followupDTO.getVisitRemark())){
            followup.setVisitRemark(followupDTO.getVisitRemark());
        }
        // todo 这里没有保证数据一致性
        if(StringUtils.isNotBlank(followupDTO.getVisitNumber())){
            followup.setVisitNumber(followupDTO.getVisitNumber());
        }
        if(StringUtils.isNotBlank(followupDTO.getDeptId())){
            followup.setDeptId(followupDTO.getDeptId());
        }
        if(StringUtils.isNotBlank(followupDTO.getDoctorId())){
            followup.setDoctorId(followupDTO.getDoctorId());
        }
        followupService.saveOrUpdate(followup);
        return Result.succ(null);
    }


}
