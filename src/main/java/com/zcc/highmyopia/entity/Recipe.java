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
@TableName("recipe")
public class Recipe implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 处方ID

    private Long doctorId;  // 医生ID
    private Long patientId;  // 患者ID
    private Long visitId;  // 就诊号
    private Long deptId;  // 科室ID

    private String regNumber;  // 挂号编号
    private String recipeNumber;  // 处方编号
    private Integer recipeType;  // 处方类型

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billingTime;  // 开方时间
}