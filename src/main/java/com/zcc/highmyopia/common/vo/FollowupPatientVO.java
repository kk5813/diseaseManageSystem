package com.zcc.highmyopia.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowupPatientVO {
    private Long followupId;
    private String patientId;
    private String planVisitDate;
    private String visitPlan;
    private Integer visitResult;
    private String visitContent;
    private String visitRemark;
    private String visitDate;
    private String patientName;
    private String gender;
    private String telephone;
    private String idNumber;
}
