package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-12-21:26
 * @Description:
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("followup_template")
@NoArgsConstructor
@AllArgsConstructor
public class FollowupTemplate {
    private static final long serialVersionUID = 2L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String deptId;

    private String templateName;

    private int intervalValue; // 下次来访间隔时间

    private String visitPlan; // 计划说明

    private int visitResult; // 方法结果， 0 待来访， -1 删除记录，1 已来访

    private String visitContent;

    private String visitRemark;

    private int isActive;

    private String creator;

    @TableField("modifier")
    private String modifier;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;

    public static Followup TemplateToLocalFollowup(FollowupTemplate followupTemplate){
        Followup build = Followup.builder()
                .visitPlan(followupTemplate.getVisitPlan())
                .visitResult(followupTemplate.getVisitResult())
                .visitRemark(followupTemplate.getVisitRemark())
                .visitContent(followupTemplate.getVisitContent())
                .build();
        return build;
    }
}
