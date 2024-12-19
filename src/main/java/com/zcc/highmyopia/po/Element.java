package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("element")
public class Element {

    private String id;             // 病历ID
    private Long patientId;        // 患者ID

    private String mainAppeal;      // 主诉
    private String pastHistory;     // 既往史
    private String presentIllness; // 现病史
    private String allergy;        // 过敏史
    private String specialOs;      // 左眼科专科检查
    private String specialOd;      // 右眼科专科检查
    private String visitNumber;    // 就诊编号
    private String physicalExam;   // 体格检查

    private String dispose;        // 处理意见

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}
