package com.zcc.highmyopia.common.dto;

import com.zcc.highmyopia.po.Followup;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-12-20:05
 * @Description:
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode
public class FollowAddsDTO {

    private String patientId;  // 病人ID，唯一标识患者

    private String deptId; // 科室ID

    private String doctorId; // 医生ID

    private String visitNumber; // 就诊号

    private String planVisitDate;  // 计划随访日期

    private String visitPlan;  // 随访计划描述

    private int visitResult;  // 随访结果，可能是成功/失败等

    private String visitContent;  // 随访内容详情，可能是医生对随访的记录

    private String visitRemark;  // 备注信息，便于补充随访中的特殊情况

    public static Followup dtoToPo(FollowAddsDTO followAddsDTO){
        Followup followup = new Followup();
        followup.setPatientId(followAddsDTO.getPatientId());
        followup.setDeptId(followAddsDTO.getDeptId());
        followup.setDoctorId(followAddsDTO.getDoctorId());
        followup.setVisitNumber(followAddsDTO.getVisitNumber());
        followup.setPlanVisitDate(followAddsDTO.getPlanVisitDate());
        followup.setVisitPlan(followAddsDTO.getVisitPlan());
        followup.setVisitResult(followAddsDTO.getVisitResult()); // 0 未随访
        followup.setVisitRemark(followAddsDTO.getVisitRemark());
        followup.setVisitContent(followAddsDTO.getVisitContent());
        return followup;
    }
}
