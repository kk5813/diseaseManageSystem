package com.zcc.highmyopia.hospital.entity;

import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.CheckResults;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckResultsEntity {

    private String patientName;           // 患者姓名
    private Integer isUrgent;                 // 是否紧急，0表示非紧急，1表示紧急
    private String labItemName;           // 检验项目名称
    private String reportName;            // 报告名称
    private Long patientId;               // 患者ID
    private String labItemCode;           // 检验项目代码
    private String refRange;              // 参考范围
    private String labResultSignName;     // 检验结果标识
    private String labFinalValue;         // 检验结果
    private String visitingNo;            // 就诊编号
    private String sexName;               // 性别名称，"男"或"女"
    private String patientBirthday;       // 患者出生日期，格式为 "YYYY-MM-DD HH:MM:SS"
    private String labResultUnitName;     // 检验结果单位名称
    private Long id;                      // 检验结果ID
    private String auditDate;             // 审核日期，格式为 "YYYY-MM-DD HH:MM:SS"

    public static CheckResults entityToPo(CheckResultsEntity checkResultEntity) {
        CheckResults checkResult = new CheckResults();
        checkResult.setId(checkResultEntity.getId());
        checkResult.setPatientId(checkResultEntity.getPatientId());
        checkResult.setIsUrgent(checkResultEntity.getIsUrgent());
        checkResult.setLabItemName(checkResultEntity.getLabItemName());
        checkResult.setLabItemCode(checkResultEntity.getLabItemCode());
        checkResult.setReportName(checkResultEntity.getReportName());
        checkResult.setRefRange(checkResultEntity.getRefRange());
        checkResult.setLabResultSignName(checkResultEntity.getLabResultSignName());
        checkResult.setLabFinalValue(checkResultEntity.getLabFinalValue());
        checkResult.setVisitingNo(checkResultEntity.getVisitingNo());
        checkResult.setLabResultUnitName(checkResultEntity.getLabResultUnitName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        checkResult.setAuditDate(LocalDateTime.parse(checkResultEntity.getAuditDate(), formatter));
        return checkResult;
    }
}
