package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.hospital.entity.RecipeEntity;
import com.zcc.highmyopia.mapper.IOrderDetailMapper;
import com.zcc.highmyopia.po.*;
import com.zcc.highmyopia.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/19
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "处方接口管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/recipe")
public class RecipeController {
    @Autowired
    private final IRecipeService recipeService;
    @Autowired
    private final IPatientsService patientsService;
    @Autowired
    private final IDeptService deptService;
    @Autowired
    private final IDoctorService doctorService;

    @Autowired
    private final IOrderDetailService orderDetailService;
    @ApiOperation("根据用户ID查询门诊记录")
    @GetMapping("/find/{patientId}")
    @RequiresAuthentication
    public Result find(@PathVariable(name = "patientId") Long patientId){
        log.info("根据用户ID查询门诊记录 patient：{}", patientId);
        List<Recipe> list = recipeService.list(new LambdaQueryWrapper<Recipe>()
                .eq(Recipe::getId, patientId));
        List<RecipeEntity> recipeEntities = list.stream().map(recipe -> {
            Patients patientById = patientsService.getPatientById(recipe.getPatientId());
            Dept deptById = deptService.getDeptById(recipe.getDeptId());
            Doctor doctorById = doctorService.getDoctorById(recipe.getDoctorId());
            List<OrderDetail> orderDetails = orderDetailService.searchOrderDetail(recipe.getRecipeNumber());
            return RecipeEntity.poToVo(recipe,deptById.getDeptName(),doctorById.getDoctorName(), patientById.getName(),orderDetails);}
        ).collect(Collectors.toList());
        return Result.succ(recipeEntities);
    }
}
