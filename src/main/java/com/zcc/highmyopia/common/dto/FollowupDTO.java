package com.zcc.highmyopia.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName FollowupDTO
 * @Description
 * @Author aigao
 * @Date 2024/12/29 15:40
 * @Version 1.0
 */
@Data
public class FollowupDTO {
    private Long id;  // 唯一标识符

    private String patientId;  // 病人ID，唯一标识患者

    @JsonFormat(pattern = "yyyy-MM-dd")  // 将日期格式化为"yyyy-MM-dd"，便于前端显示
    private LocalDateTime planVisitDate;  // 计划随访日期

    private String visitPlan;  // 随访计划内容描述

    private int visitResult;  // 随访结果，可能是成功/失败等

    private String visitContent;  // 随访内容详情，可能是医生对随访的记录

    private String visitRemark;  // 备注信息，便于补充随访中的特殊情况

    private LocalDateTime visitDate;  // 实际随访日期
}