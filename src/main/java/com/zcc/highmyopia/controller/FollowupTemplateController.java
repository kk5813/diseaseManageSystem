package com.zcc.highmyopia.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.dto.FollowupTemplateDTO;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.common.vo.FollowupTemplateVO;
import com.zcc.highmyopia.mapper.IFollowupTemplateMapper;
import com.zcc.highmyopia.po.FollowupTemplate;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IFollowupTemplateService;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.util.JwtUtils;
import com.zcc.highmyopia.util.ThrowUtils;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-12-21:53
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "随访添加模板的管理")
@RequestMapping("/api/${app.config.api-version}/followupTemplate")
public class FollowupTemplateController {
    @Autowired
    IFollowupTemplateService followupTemplateService;
    @Autowired
    IFollowupTemplateMapper followupTemplateMapper;

    @GetMapping("/search")
    @RequiresAuthentication
    @ApiOperation(value = "模糊查询")
    public Result searchFollowupTemplate(@RequestParam(defaultValue = "") String dateStart, @RequestParam(defaultValue = "") String dateEnd,
                                         @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(defaultValue = "") String deptName, @RequestParam(defaultValue = "") String templateName,
                                         @RequestParam(defaultValue = "") String modifyName, @RequestParam(defaultValue = "0") int intervalValue,
                                         @RequestParam(defaultValue = "") String visitPlan
    ){
        IPage<FollowupTemplateVO> followupTemplateVOIPage = followupTemplateService.searchFollowupTemplate(dateStart, dateEnd, pageNo, pageSize, deptName, templateName, modifyName, intervalValue, visitPlan);
        return Result.succ(followupTemplateVOIPage);
    }

    private final IUserService userService;

    private final JwtUtils jwtUtils;

    @PostMapping("/addFollowupTemplate")
    @ApiOperation(value = "添加随访模板")
    @RequiresAuthentication
    public Result addFollowupTemplate(@RequestBody FollowupTemplateDTO followupTemplateDTO, HttpServletRequest request){
        ThrowUtils.throwIf(followupTemplateDTO.getIntervalValue() <= 0, new BusinessException(ResultCode.FOLLOWUP_TEMPLATE_INTERVALVALUE));
        ThrowUtils.throwIf(StringUtils.isBlank(followupTemplateDTO.getVisitPlan()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺失visitPlan!，添加随访模板至少需要4项参数：{IntervalValue,VisitPlan,DeptID,templateValue}"));
        ThrowUtils.throwIf(StringUtils.isBlank(followupTemplateDTO.getDeptId()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺失DeptId!，添加随访模板至少需要4项参数：{IntervalValue,VisitPlan,DeptID,templateValue}"));
        ThrowUtils.throwIf(StringUtils.isBlank(followupTemplateDTO.getTemplateName()), new BusinessException(ResultCode.PARAMS_FIELD_MISSING, "缺失TemplateName!，添加随访模板至少需要4项参数：{IntervalValue,VisitPlan,DeptID,templateValue}"));
        FollowupTemplate followupTemplate = FollowupTemplateDTO.DTOToPo(followupTemplateDTO);

        String jwtToken = request.getHeader("Authorization");
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);
        String userId = claimByToken.getSubject();
        String userName = userService.getUserBridgeRedis(userId);
        followupTemplate.setVisitResult(Constants.FollowupStatus.NOT_FOLLOW);
        followupTemplate.setCreateTime(LocalDateTime.now());
        followupTemplate.setUpdateTime(LocalDateTime.now());
        followupTemplate.setCreator(userName);
        followupTemplate.setModifier(userName);
        followupTemplate.setIsActive(1);
        // 保证name的唯一性
        try{
            followupTemplateService.saveOrUpdate(followupTemplate);
        }catch (RuntimeException e){
            throw new BusinessException(ResultCode.FOLLOWUP_TEMPLATE_NAME);
        }
        return Result.succ(FollowupTemplateVO.FollowupTemplateToVo(followupTemplate));
    }
    @PostMapping("/editFollowupTemplate")
    @ApiOperation(value = "修改随访模板")
    @RequiresAuthentication
    public Result editFollowupTemplate(@RequestBody FollowupTemplateDTO followupTemplateDTO, HttpServletRequest request){
        ThrowUtils.throwIf(StringUtils.isBlank(followupTemplateDTO.getId()), new BusinessException(ResultCode.ID_FIELD_MISSING, "缺少随访模板ID"));
        ThrowUtils.throwIf(followupTemplateDTO.getIntervalValue() < 0, new BusinessException(ResultCode.FOLLOWUP_TEMPLATE_INTERVALVALUE));
        String jwtToken = request.getHeader("Authorization");
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);
        String userId = claimByToken.getSubject();
        String userName = userService.getUserBridgeRedis(userId);
        FollowupTemplate template = followupTemplateService.getOne(new LambdaQueryWrapper<FollowupTemplate>().eq(FollowupTemplate::getId, followupTemplateDTO.getId()));
        if(template == null){
            return Result.fail("操作失败，没有ID=" + followupTemplateDTO.getId() + "的模板");
        }
        if(StringUtils.isNotBlank(followupTemplateDTO.getDeptId()) && followupTemplateDTO.getDeptId().trim().matches("[0-9]+")){
            template.setDeptId(followupTemplateDTO.getDeptId());
        }
        if(StringUtils.isNotBlank(followupTemplateDTO.getTemplateName())){
            template.setTemplateName(followupTemplateDTO.getTemplateName());
        }
        if(followupTemplateDTO.getIntervalValue() > 0){
            template.setIntervalValue(followupTemplateDTO.getIntervalValue());
        }
        if(StringUtils.isNotBlank(followupTemplateDTO.getVisitPlan())){
            template.setVisitPlan(followupTemplateDTO.getVisitPlan());
        }
        if(StringUtils.isNotBlank(followupTemplateDTO.getVisitContent())){
            template.setVisitContent(followupTemplateDTO.getVisitContent());
        }
        if(StringUtils.isNotBlank(followupTemplateDTO.getVisitRemark())){
            template.setVisitRemark(followupTemplateDTO.getVisitRemark());
        }
        template.setModifier(userName);
        template.setUpdateTime(LocalDateTime.now());
        try{
            followupTemplateService.saveOrUpdate(template);
        }catch (RuntimeException e){
            throw new BusinessException(ResultCode.FOLLOWUP_TEMPLATE_NAME);
        }
        return Result.succ(FollowupTemplateVO.FollowupTemplateToVo(template));
    }
    @GetMapping("/invalidFollowupTemplate/{templateId}")
    @ApiOperation(value = "删除随访模板")
    @RequiresAuthentication
    public Result deleteFollowupTemplate(@PathVariable(name = "templateId") String templateId, HttpServletRequest request){
        ThrowUtils.throwIf(StringUtils.isBlank(templateId) && !templateId.trim().matches("[0-9]+"), ResultCode.FOLLOWUP_TEMPLATE_ID);
        String jwtToken = request.getHeader("Authorization");
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);
        String userId = claimByToken.getSubject();
        String userName = userService.getUserBridgeRedis(userId);
        FollowupTemplate template = followupTemplateService.getOne(new LambdaQueryWrapper<FollowupTemplate>().eq(FollowupTemplate::getId, templateId.trim()));
        if(template != null){
            template.setModifier(userName);
            template.setUpdateTime(LocalDateTime.now());
            template.setIsActive(0);
            followupTemplateService.saveOrUpdate(template);
        }
        return Result.succ(null);

    }
}
