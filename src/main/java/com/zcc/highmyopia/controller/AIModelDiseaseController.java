package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.IDiagnoseService;
import com.zcc.highmyopia.AIDiagnose.entity.request.DiagnoseRequestDTO;
import com.zcc.highmyopia.AIDiagnose.entity.response.DiagnoseResponse;
import com.zcc.highmyopia.AIDiagnose.service.AIDiagnoseService;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.util.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AIModelDiseaseController
 * @Description
 * @Author aigao
 * @Date 2026/1/18 23:58
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "AI智能诊断类别")
@RestController
@RequestMapping("/api/${app.config.api-version}/AIDisease")
public class AIModelDiseaseController {
    private final AIDiagnoseService diagnoseService;
    @Value("${hospital.filePath}")
    private String filePath;
    private final DateTimeFormatter formatterNoSplit = DateTimeFormatter.ofPattern("yyyyMMdd");
    @PostMapping("diagnose")
    @ApiOperation("模型诊断接口")
    public Result diagnose(@RequestBody DiagnoseRequestDTO diagnose) throws Exception {
        log.info("诊断接口运行");
        // 校验参数，权限参数为 userID
        Integer diseaseId = diagnose.getDiseaseId();
        String visitNumber = diagnose.getVisitNumber();
        ThrowUtils.throwIf(diseaseId == null || diseaseId <= 0, 4001, "diseaseId参数错误");
        ThrowUtils.throwIf(visitNumber == null || visitNumber.isEmpty(), 4001, "visitNumber参数不能为空");
        try {
            // 提取 visitNumber 中的日期部分（假设格式为 XXyyyyMMdd...）
            // 提取从索引2开始的8位字符
            String datePart = visitNumber.substring(2, 10);
            log.info("datePart = " + datePart);
            LocalDate.parse(datePart,formatterNoSplit);
        } catch (Exception e) {
           ThrowUtils.throwIf(true,4001,"visitNumber参数格式错误，无法提取日期部分");
        }
        List<DiagnoseResponse> diagnoseResponses = diagnoseService.diagnose(diagnose);

        for (DiagnoseResponse diagnoseResponse : diagnoseResponses) {
            Map<String, String> urls = diagnoseResponse.getUrls();
            urls.replaceAll((k, v) -> GetRelativePath(v));
        }
        return Result.succ(diagnoseResponses);
    }

    public String GetRelativePath(String filePath_absolute){
        Path absolute = Paths.get(filePath_absolute);  // "E:/xxx/xxx/xxx"
        Path relative = Paths.get(filePath);          // "E:/xxx/"
        // 计算 path1 到 absolutePath2 的相对路径
        return String.valueOf(relative.relativize(absolute));
    }
}