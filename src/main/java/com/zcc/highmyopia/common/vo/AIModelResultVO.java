package com.zcc.highmyopia.common.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcc.highmyopia.po.AIModelResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
        if(StringUtils.isNotBlank(aiModelResult.getUrls())){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<String> urlsList = objectMapper.readValue(aiModelResult.getUrls(), new TypeReference<List<String>>(){});
                modelResultVO.setUrls(urlsList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                modelResultVO.setUrls(null);
            }
        }else{
            modelResultVO.setUrls(null);
        }
//        if (aiModelResult.getUrls() != null && !aiModelResult.getUrls().isEmpty()){
//            String urlsAsString = "[" + String.join(",", aiModelResult.getUrls()) + "]";
//            aiModelResult.setUrls(urlsAsString);
//        }
        return modelResultVO;
    }
}
