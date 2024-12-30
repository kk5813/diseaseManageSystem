package com.zcc.highmyopia.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcc.highmyopia.po.Followup;
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

    private String planVisitDate;  // 计划随访日期

    private String visitPlan;  // 随访计划内容描述

    private int visitResult;  // 随访结果，可能是成功/失败等

    private String visitContent;  // 随访内容详情，可能是医生对随访的记录

    private String visitRemark;  // 备注信息，便于补充随访中的特殊情况

    private String visitDate;  // 实际随访日期

    public static Followup dtoToPo(FollowupDTO followupDTO){
        Followup followup = new Followup();
        followup.setId(followup.getId());
        followup.setPatientId(followupDTO.getPatientId());
        followup.setPlanVisitDate(followupDTO.getPlanVisitDate());
        followup.setVisitPlan(followupDTO.getVisitPlan());
        followup.setVisitResult(followupDTO.getVisitResult());
        followup.setVisitDate(followupDTO.getVisitDate());
        followup.setVisitRemark(followupDTO.getVisitRemark());
        followup.setVisitContent(followupDTO.getVisitContent());
        return followup;
    }
}
