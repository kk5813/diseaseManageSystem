package com.zcc.highmyopia.hospital.entity;

import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.CheckResults;
import com.zcc.highmyopia.po.ReportFiles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckReportsEntity {

    private String itemCode;       // 检查项目编码
    private String patientId;      // 患者ID
    private List<ReportFiles> files; // 相关文件信息数组，包含文件类型和下载URL
    private String itemName;       // 检查项目名称
    private String id;             // 检查ID，通常为研究UID
    private String patientSid;     // 患者SID
    private String visitNumber;    // 就诊编号
    private String checkTime;      // 检查时间，格式为 "YYYY-MM-DD HH:MM:SS"

    public static CheckReports entityToPo(CheckReportsEntity checkReportsEntity) {
        CheckReports checkReports = new CheckReports();
        checkReports.setPatientId(Long.valueOf(checkReportsEntity.getPatientId())); // 转换为 Long 类型
        checkReports.setItemCode(checkReportsEntity.getItemCode());
        checkReports.setItemName(checkReportsEntity.getItemName());
        // 转换日期字符串为 LocalDateTime
        checkReports.setCheckTime(LocalDateTime.parse(checkReportsEntity.getCheckTime()));
        checkReports.setVisitNumber(checkReportsEntity.getVisitNumber());
        return checkReports;
    }
}
