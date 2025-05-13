package com.zcc.highmyopia.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcc.highmyopia.po.FollowupTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-12-22:12
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowupTemplateVO {
    private Long id;  // 唯一标识符

    private String templateName; // 模板名称

    private String deptName;  // 科室名称

    private int intervalValue;  // 计划随访日期

    private String visitPlan;  // 随访计划内容描述

    private int visitResult;  // 随访结果，可能是成功/失败等

    private String visitContent;  // 随访内容详情，可能是医生对随访的记录

    private String visitRemark;  // 备注信息，便于补充随访中的特殊情况

    private String modifier; // 目标的修改者或创建者

    public static FollowupTemplateVO FollowupTemplateToVo(FollowupTemplate followupTemplate){
        FollowupTemplateVO build = FollowupTemplateVO.builder()
                .id(followupTemplate.getId())
                .templateName(followupTemplate.getTemplateName())
                .intervalValue(followupTemplate.getIntervalValue())
                .visitPlan(followupTemplate.getVisitPlan())
                .visitResult(followupTemplate.getVisitResult())
                .visitContent(followupTemplate.getVisitContent())
                .visitRemark(followupTemplate.getVisitRemark())
                .modifier(followupTemplate.getModifier())
                .build();
        return build;
    }
}
