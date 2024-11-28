package com.zcc.highmyopia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("patients")
public class Patients implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 患者ID

    @NotBlank(message = "患者姓名不能为空")
    private String name;  // 患者姓名

    private Integer sex;  // 性别标识
    private String sexName;  // 性别名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;  // 出生日期

    private String idNumber;  // 身份证号
    private String phone;  // 电话号码

    private Integer status;  // 逻辑删除状态
}