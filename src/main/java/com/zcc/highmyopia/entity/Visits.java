package com.zcc.highmyopia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("visits")
public class Visits implements Serializable {
    
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 就诊号

    private Long patientId;  // 患者ID
    private Long doctorId;  // 医生ID
    private Long deptId;  // 科室ID
    private Long siteId;  // 眼别ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime diagTime;  // 诊断时间

    private Integer diagOrder;  // 诊断序号
    private String diagName;  // 诊断名称
    private String diagCode;  // 诊断编码
}