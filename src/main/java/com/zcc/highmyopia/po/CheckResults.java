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
@TableName("check_results")
public class CheckResults implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 检验结果ID

    private Long patientId;  // 患者ID
    private Integer isUrgent;                 // 是否紧急，0表示非紧急，1表示紧急

    private String labItemName;  // 检验项目名称
    private String labItemCode;  // 检验项目代码
    private String reportName;            // 报告名称

    private String refRange;  // 参考范围
    private String labResultSignName;     // 检验结果标识
    private String labFinalValue;  // 检验结果
    private String visitingNo;  // 就诊编号
    private String labResultUnitName;     // 检验结果单位名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditDate;  // 审核日期

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime  createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}