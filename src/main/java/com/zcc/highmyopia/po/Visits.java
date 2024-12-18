package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
    private String visitNumber; // 就诊号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime diagTime;  // 诊断时间

    private Integer diagOrder;  // 诊断序号
    private String diagName;  // 诊断名称
    private String diagCode;  // 诊断编码

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime  createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}