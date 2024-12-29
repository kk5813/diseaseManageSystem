package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jboss.marshalling.util.IntReadField;

import java.time.LocalDateTime;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("model_line")
public class ModelLine {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private Integer diseaseId;

    private Integer modelFrom;

    private Integer modelTo;

    private String limitValue;

    private String desc;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}
