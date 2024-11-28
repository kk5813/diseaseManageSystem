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
@TableName("lab_results")
public class LabResult implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 检验结果ID

    private Long patientId;  // 患者ID
    private Long visitId;  // 就诊号
    private String labItemName;  // 检验项目名称
    private String labItemCode;  // 检验项目代码

    private String refRange;  // 参考范围
    private String labFinalValue;  // 检验结果
    private String visitingNo;  // 就诊编号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditDate;  // 审核日期
}