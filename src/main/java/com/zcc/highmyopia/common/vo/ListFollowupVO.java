package com.zcc.highmyopia.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/***
 * 随访相关的数据传输对象 (VO)
 * 用于封装前端传来的随访信息，或将数据传给前端
 */
@Data  // Lombok注解，自动生成getter、setter、toString等方法
public class ListFollowupVO {

    private Long id;  // 唯一标识符

    private String patientId;  // 病人ID，唯一标识患者

    @JsonFormat(pattern = "yyyy-MM-dd")  // 将日期格式化为"yyyy-MM-dd"，便于前端显示
    private LocalDateTime planVisitDate;  // 计划随访日期

    private String visitPlan;  // 随访计划内容描述

    private int visitResult;  // 随访结果，可能是成功/失败等

    private String visitContent;  // 随访内容详情，可能是医生对随访的记录

    private String visitRemark;  // 备注信息，便于补充随访中的特殊情况

    private LocalDateTime visitDate;  // 实际随访日期



    private String patientName;  // 病人的名字

    private String gender;  // 病人的性别

    private String telephone;  // 病人的联系电话

    @JsonFormat(pattern = "yyyy-MM-dd")  // 将日期格式化为"yyyy-MM-dd"，便于前端显示
    private LocalDateTime nextVisitDate;  // 下次随访日期
}
