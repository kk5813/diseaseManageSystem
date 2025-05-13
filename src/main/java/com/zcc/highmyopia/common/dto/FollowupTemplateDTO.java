package com.zcc.highmyopia.common.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zcc.highmyopia.po.FollowupTemplate;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-12-23:38
 * @Description:
 * @Version: 1.0
 */
@Data
public class FollowupTemplateDTO {

    private String id;

    private String deptId;

    private String templateName;

    private int intervalValue; // 下次来访间隔时间

    private String visitPlan; // 随访计划

    private int visitResult; // 方法结果， 0 待来访， -1 删除记录，1 已来访

    private String visitContent; // 随访内容

    private String visitRemark; // 随访备注

    public static FollowupTemplate DTOToPo(FollowupTemplateDTO followupTemplateDTO){
        FollowupTemplate build = FollowupTemplate.builder()
                .deptId(followupTemplateDTO.getDeptId())
                .templateName(followupTemplateDTO.getTemplateName())
                .intervalValue(followupTemplateDTO.getIntervalValue())
                .visitPlan(followupTemplateDTO.getVisitPlan())
                .visitRemark(followupTemplateDTO.getVisitRemark())
                .visitContent(followupTemplateDTO.getVisitContent())
                .visitResult(followupTemplateDTO.getVisitResult())
                .build();
        if(StringUtils.isNotBlank(followupTemplateDTO.getId()) && followupTemplateDTO.getId().trim().matches("[0-9]+")){
            build.setId(Long.valueOf(followupTemplateDTO.getId().trim()));
        }
        return build;
    }
}
