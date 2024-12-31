package com.zcc.highmyopia.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.IDiagnoseService;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.ModelDisease;
import com.zcc.highmyopia.po.Site;
import com.zcc.highmyopia.service.IModelDiseaseService;
import com.zcc.highmyopia.service.ISiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "智能诊断类别")
@RestController
@RequestMapping("/api/${app.config.api-version}/disease")
public class ModelDiseaseController {

    private final IModelDiseaseService modelDiseaseService;
    private final IDiagnoseService diagnoseService;

    @GetMapping("get_disease")
    @ApiOperation("获取配置的疾病列表")
    @RequiresAuthentication
    public Result getList(){
        List<ModelDisease> list = modelDiseaseService.list();
        return Result.succ(list);
    }

    @PostMapping("diagnose")
    @ApiOperation("获取配置的疾病列表")
    @RequiresAuthentication
    public Result diagnose(@RequestBody DiagnoseEntity diagnose){
        List<DiagnoseResultEntity> diagnoseResultEntityList = diagnoseService.diagnose(diagnose);
        return Result.succ(diagnoseResultEntityList);
    }

}