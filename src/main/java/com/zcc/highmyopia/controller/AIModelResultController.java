package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcc.highmyopia.common.dto.AIModelResultDTO;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.po.AIModelResult;
import com.zcc.highmyopia.service.IAIModelResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Api(tags = "AI模型诊断结果管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/ai_model_result")
public class AIModelResultController {

    @Autowired
    private IAIModelResultService aiModelResultService;

    @PostMapping("/add")
    @RequiresAuthentication
    @ApiOperation(value = "添加 AI 模型结果数据")
    public Result addAIModelResult(@RequestBody AIModelResultDTO aiModelResultDTO) {
        AIModelResult aiModelResult = new AIModelResult();
        // 设置基础字段
        aiModelResult.setPatientId(aiModelResultDTO.getPatientId());
        aiModelResult.setVisitNumber(aiModelResultDTO.getVisitNumber());
        aiModelResult.setUserId(aiModelResultDTO.getUserId());
        aiModelResult.setDiagnosisProcessId(aiModelResultDTO.getDiagnosisProcessId());
        aiModelResult.setDescription(aiModelResultDTO.getDescription());
        // 将 List<String> urls 转换为逗号分隔的字符串
        if (aiModelResultDTO.getUrls() != null && !aiModelResultDTO.getUrls().isEmpty()) {
            try {
                // 将 List<String> 转换为合法的 JSON 数组格式
                String urlsAsJson = new ObjectMapper().writeValueAsString(aiModelResultDTO.getUrls());
                log.info("Urls as JSON: " + urlsAsJson);
                aiModelResult.setUrls(urlsAsJson);
            } catch (JsonProcessingException e) {
                throw new BusinessException(ResultCode.JSON_PARSE_ERROR);
            }
        }
        // 设置时间字段
        aiModelResult.setCreateTime(LocalDateTime.now());
        aiModelResult.setUpdateTime(LocalDateTime.now());

        boolean result = aiModelResultService.saveAIModelResult(aiModelResult);
        return result ? Result.succ("添加成功") : Result.fail("添加失败");
    }

    @GetMapping("/list")
    @RequiresAuthentication
    @ApiOperation(value = "多条件模糊查询，支持分页")
    public Result listAIModelResults(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "visitNumber", required = false) String visitNumber,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "diagnosisProcessId", required = false) Long diagnosisProcessId) {

        Page<AIModelResult> pageRequest = new Page<>(page, size);
        IPage<AIModelResult> aiModelResultIPage = aiModelResultService.selectByCondition(pageRequest, patientId, visitNumber, description, userId, diagnosisProcessId);
        return Result.succ(aiModelResultIPage);
    }
}
