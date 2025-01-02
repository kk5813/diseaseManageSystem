package com.zcc.highmyopia.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.errorprone.annotations.Var;
import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.IDiagnoseService;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.AIModelResult;
import com.zcc.highmyopia.po.ModelDisease;
import com.zcc.highmyopia.po.Site;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IAIModelResultService;
import com.zcc.highmyopia.service.IModelDiseaseService;
import com.zcc.highmyopia.service.ISiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.zcc.highmyopia.controller.CheckReportsController.LocalPathToVirtualPath;

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
    private final IAIModelResultService aiModelResultService;
    @Value("${hospital.filePath}")
    private String filePath;

    @GetMapping("get_disease")
    @ApiOperation("获取配置的疾病列表")
    @RequiresAuthentication
    public Result getList(){
        List<ModelDisease> list = modelDiseaseService.list();
        return Result.succ(list);
    }

    @PostMapping("diagnose")
    @ApiOperation("模型诊断接口")
    @RequiresAuthentication
    public Result diagnose(@RequestBody DiagnoseEntity diagnose){
        log.info("诊断接口运行");
        List<DiagnoseResultEntity> diagnoseResultEntityList = diagnoseService.diagnose(diagnose);
        // 诊断接口写库表
        AIModelResult aiModelResult = new AIModelResult();
        aiModelResult.setPatientId(Long.valueOf(diagnose.getPatientId()));
        aiModelResult.setUserId(aiModelResult.getUserId());
        // 就诊号？
        // aiModelResult.setVisitNumber();
        List<String> urls = diagnoseResultEntityList.stream()
                .map(DiagnoseResultEntity::getUrl)
                .collect(Collectors.toList());
        aiModelResult.setUrls(JSON.toJSONString(urls));
        List<String> result = diagnoseResultEntityList.stream()
                .map(DiagnoseResultEntity::getResultInfo)
                .collect(Collectors.toList());
        aiModelResult.setDescription(result.toString());
        aiModelResult.setDiagnosisProcessId(Long.valueOf(diagnose.getDiseaseId()));
        aiModelResultService.save(aiModelResult);
        diagnoseResultEntityList.forEach(e -> {e.setUrl(GetRelativePath(e.getUrl()));});
        return Result.succ(diagnoseResultEntityList);
    }

    public String GetRelativePath(String filePath_absolute){
        Path absolute = Paths.get(filePath_absolute);  // "E:/xxx/xxx/xxx"
        Path relative = Paths.get(filePath);          // "E:/xxx/"
        // 计算 path1 到 absolutePath2 的相对路径
        return String.valueOf(absolute.relativize(relative));
    }

}