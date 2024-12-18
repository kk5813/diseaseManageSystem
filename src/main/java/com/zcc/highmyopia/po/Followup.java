package com.zcc.highmyopia.po;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liangyue
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("followup")
public class Followup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String patientId;

    private LocalDateTime planVisitDate;

    private String visitPlan;

    private Boolean visitResult;

    private String visitContent;

    private String visitRemark;

    private LocalDateTime visitDate;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime  createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;

}
