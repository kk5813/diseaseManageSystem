package com.zcc.highmyopia.hospital.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description 与第三方接口进行操作的实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
}
