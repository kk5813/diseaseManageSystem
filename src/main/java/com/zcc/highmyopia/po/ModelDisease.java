package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("diagnose_disease")
public class ModelDisease {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private String name;

    private String desc;

    private String input;

    private Integer startNode;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}
