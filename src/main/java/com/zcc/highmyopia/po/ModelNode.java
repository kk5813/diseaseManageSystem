package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("model_node")
public class ModelNode {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private Integer diseaseId;

    private String input;

    private String name;

    private String api;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;

}
