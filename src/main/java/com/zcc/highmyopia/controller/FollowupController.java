package com.zcc.highmyopia.controller;


import com.zcc.highmyopia.common.dto.ListFollowup;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.Followup;
import com.zcc.highmyopia.mapper.IFollowupMapper;
import com.zcc.highmyopia.service.IFollowupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    /**
     * 今日未随访
     * @return
     */
    @GetMapping("/todayUndo")
    @ApiOperation(value = "今日未随访")
    @RequiresAuthentication
    public Result todayUndoFollowup() {
        return Result.succ(followupMapper.selectToDayFollowUpList());
    }

    /**
     *
     * 超期未随访
     * @return
     */
    @GetMapping("/overdue")
    @ApiOperation(value = "超期未随访")
    @RequiresAuthentication
    public Result overdueFollowup() {
        return Result.succ(followupMapper.selectOverdueFollowUpList());
    }

    /**
     * 全部未随访
     * @return
     */
    @GetMapping("/undo")
    @RequiresAuthentication
    @ApiOperation(value = "全部未随访")
    public Result undoFollowup() {
        return Result.succ(followupMapper.selectUndoFollowUpList());
    }

    /**
     *
     * 完成一次随访，如果随访失败，还会自动添加下次随访
     * @param listFollowup
     * @return
     */
    @PostMapping("/editFollowup")
    @ApiOperation(value = "完成一次随访，如果随访失败，还会自动添加下次随访")
    @RequiresAuthentication
    public Result editFollowup(@RequestBody ListFollowup listFollowup) {
        Followup followup = followupService.getById(listFollowup.getId());
        followup.setVisitPlan(listFollowup.getVisitPlan());
        followup.setVisitResult(listFollowup.getVisitResult());
        followup.setVisitContent(listFollowup.getVisitContent());
        followup.setVisitRemark(listFollowup.getVisitRemark());
        followup.setVisitDate(LocalDateTime.now());
        followupService.saveOrUpdate(followup);
        if(!listFollowup.getVisitResult()) {
            Followup temp = new Followup();
            temp.setPlanVisitDate(listFollowup.getNextVisitDate());
            temp.setPatientId(followup.getPatientId());
            followupService.saveOrUpdate(temp);
        }
        return Result.succ(null);
    }


}
