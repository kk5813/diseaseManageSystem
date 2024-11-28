package com.zcc.highmyopia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("doctor")
public class Doctor implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 医生ID

    @NotBlank(message = "医生姓名不能为空")
    private String doctorName;  // 医生姓名

    private Integer isDelete;  // 逻辑删除标识
    private Integer status;  // 逻辑删除状态
}