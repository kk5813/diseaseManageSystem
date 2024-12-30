package com.zcc.highmyopia.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @ClassName AIModelResultDTO
 * @Description
 * @Author aigao
 * @Date 2024/12/30 23:53
 * @Version 1.0
 */
@Data
public class AIModelResultDTO {
    private Long patientId;
    private String visitNumber;
    private Long userId;
    private Long diagnosisProcessId;
    private String description;
    // 接收多个 URL
    private List<String> urls;
}
