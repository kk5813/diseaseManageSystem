package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("doctor")
public class Doctor implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 医生ID

    private Long siteId; // 科室ID
    @NotBlank(message = "医生姓名不能为空")
    private String doctorName;  // 医生姓名

    private Integer status;  // 逻辑删除状态

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}