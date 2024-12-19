package com.zcc.highmyopia.hospital.entity;

import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.Visits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description 与第三方接口进行操作的实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitEntity{

    private String birthday;  // 出生日期
    private String patientName;  // 病人姓名
    private String deptName;  // 科室名称
    private Long patientId;  // 病人ID
    private String diagTime;  // 诊断时间
    private String visitNumber;  // 就诊号
    private Integer diagOrder;  // 诊断顺序
    private Integer sex;  // 性别
    private Long deptId;  // 科室ID
    private String siteName;  // 眼部部位
    private String diagName;  // 诊断名称
    private String diagCode;  // 诊断编码
    private String doctorName;  // 医生姓名
    private Long doctorId;  // 医生ID
    private String sexName;  // 性别名称
    private Long siteId;  // 眼部部位ID


    public static Visits entityToPo(VisitEntity visitEntity) {
        Visits visits = new Visits();
        visits.setPatientId(visitEntity.getPatientId());
        visits.setDoctorId(visitEntity.getDoctorId());
        visits.setDeptId(visitEntity.getDeptId());
        visits.setSiteId(visitEntity.getSiteId());
        visits.setVisitNumber(visitEntity.getVisitNumber());
        visits.setDiagOrder(visitEntity.getDiagOrder());
        visits.setDiagName(visitEntity.getDiagName());
        visits.setDiagCode(visitEntity.getDiagCode());

        String diagTimeStr = visitEntity.getDiagTime();
        if (diagTimeStr != null && !diagTimeStr.isEmpty()) {
            try {
                visits.setDiagTime(LocalDateTime.parse(diagTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format for diagTime: " + diagTimeStr);
                visits.setDiagTime(LocalDateTime.now());  // 默认值为当前时间
            }
        } else {
            visits.setDiagTime(LocalDateTime.now());  // 默认值为当前时间
        }

        return visits;
    }

}
