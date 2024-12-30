package com.zcc.highmyopia.common.vo;

import com.zcc.highmyopia.po.AIModelResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName AIModelResultVO
 * @Description
 * @Author aigao
 * @Date 2024/12/30 23:53
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIModelResultVO {
    private Long patientId;
    private String visitNumber;
    private Long userId;
    private Long diagnosisProcessId;
    private String description;
    // 接收多个 URL
    private List<String> urls;
    private String  createTime;
    private String updateTime;

    public static AIModelResultVO poToVo(AIModelResult aiModelResult){
        AIModelResultVO modelResultVO = AIModelResultVO.builder()
                .patientId(aiModelResult.getPatientId())
                .visitNumber(aiModelResult.getVisitNumber())
                .userId(aiModelResult.getUserId())
                .diagnosisProcessId(aiModelResult.getDiagnosisProcessId())
                .createTime(aiModelResult.getCreateTime().toString())
                .updateTime(aiModelResult.getUpdateTime().toString())
                .build();
        if (aiModelResult.getUrls() != null && !aiModelResult.getUrls().isEmpty()){
            String urlsAsString = "[" + String.join(",", aiModelResult.getUrls()) + "]";
            aiModelResult.setUrls(urlsAsString);
        }
        return modelResultVO;
    }
}
