package com.zcc.highmyopia.controller;


import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.IDiagnoseService;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.ModelDisease;
import com.zcc.highmyopia.service.IAIModelResultService;
import com.zcc.highmyopia.service.IModelDiseaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final IAIModelResultService aiModelResultService;
    private final ProjectInfoAutoConfiguration projectInfoAutoConfiguration;
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
        // 1.发http 请求flask接口 pdf 转图片并识别眼别，
        // 2.活动list, 左右眼图片，然后分布进行检查。
        List<DiagnoseResultEntity> diagnoseResultEntityList = diagnoseService.diagnose(diagnose);
        // 诊断接口写库表
//        AIModelResult aiModelResult = new AIModelResult();
//        aiModelResult.setPatientId(Long.valueOf(diagnose.getPatientId()));
//        aiModelResult.setUserId(aiModelResult.getUserId());
//        // 就诊号？
//        // aiModelResult.setVisitNumber();
//        List<String> urls = diagnoseResultEntityList.stream()
//                .map(DiagnoseResultEntity::getUrl)
//                .collect(Collectors.toList());
//        aiModelResult.setUrls(JSON.toJSONString(urls));
//        List<String> result = diagnoseResultEntityList.stream()
//                .map(DiagnoseResultEntity::getResultInfo)
//                .collect(Collectors.toList());
//        aiModelResult.setDescription(result.toString());
//        aiModelResult.setDiagnosisProcessId(Long.valueOf(diagnose.getDiseaseId()));
//        aiModelResultService.save(aiModelResult);

        diagnoseResultEntityList.forEach(e -> {
            String url = e.getUrl();
            if (StringUtils.isNotBlank(url)) {
                String[] split = url.split(",");
                StringBuilder result = new StringBuilder();

                // 如果只有一个路径，直接获取相对路径
                if (split.length == 1) {
                    String relativePath = GetRelativePath(split[0]);
                    result.append(relativePath);
                } else {
                    // 如果有多个路径，遍历并处理每个路径
                    for (int i = 0; i < split.length; i++) {
                        String relativePath = GetRelativePath(split[i]);
                        if (i > 0) {
                            result.append(",");
                        }
                        result.append(relativePath);
                    }
                }

                // 更新 URL 为处理后的结果
                e.setUrl(result.toString());
            }
        });
        System.out.println(diagnoseResultEntityList);
        return Result.succ(diagnoseResultEntityList);
    }

    public String GetRelativePath(String filePath_absolute){
        Path absolute = Paths.get(filePath_absolute);  // "E:/xxx/xxx/xxx"
        Path relative = Paths.get(filePath);          // "E:/xxx/"
        // 计算 path1 到 absolutePath2 的相对路径
        return String.valueOf(relative.relativize(absolute));
    }

}