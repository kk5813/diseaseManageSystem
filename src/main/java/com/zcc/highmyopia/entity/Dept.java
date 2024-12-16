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
@TableName("dept")
public class Dept implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 科室ID

    @NotBlank(message = "科室名称不能为空")
    private String deptName;  // 科室名称

    private Integer status;  // 逻辑删除状态
}