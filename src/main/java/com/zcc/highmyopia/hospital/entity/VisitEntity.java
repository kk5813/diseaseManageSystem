package com.zcc.highmyopia.hospital.entity;

import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.Visits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    // 患者出生日期，格式为 "YYYY-MM-DD HH:MM:SS"
    private String birthday;

    // 患者姓名
    private String patientName;

    // 科室名称
    private String deptName;

    // 患者ID
    private Long patientId;

    // 诊断时间，格式为 "YYYY-MM-DD HH:MM:SS"
    private String diagTime;

    // 就诊号
    private String visitNumber;

    // 诊断序号
    private Integer diagOrder;

    // 性别标识，1表示男性
    private Integer sex;

    // 科室ID
    private Integer deptId;

    // 眼别
    private String siteName;

    // 诊断名称
    private String diagName;

    // 诊断编码
    private String diagCode;

    // 医生姓名
    private String doctorName;

    // 医生ID
    private Integer doctorId;

    // 性别名称，"男"或"女"
    private String sexName;

    // 眼别ID
    private Integer siteId;

    public static Visits entityToPo(VisitEntity visitEntity) {
        Visits visits = new Visits();
        visits.setPatientId(visitEntity.getPatientId());
        visits.setDoctorId(Long.valueOf(visitEntity.getDoctorId()));
        visits.setDeptId(Long.valueOf(visitEntity.getDeptId()));
        visits.setSiteId(Long.valueOf(visitEntity.getSiteId()));
        visits.setVisitNumber(visitEntity.getVisitNumber());
        visits.setDiagTime(LocalDateTime.parse(visitEntity.getDiagTime()));
        visits.setDiagOrder(visitEntity.getDiagOrder());
        visits.setDiagName(visitEntity.getDiagName());
        visits.setDiagCode(visitEntity.getDiagCode());
        return visits;
    }
}
