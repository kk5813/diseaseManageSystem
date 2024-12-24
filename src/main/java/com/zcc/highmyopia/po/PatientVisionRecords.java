package com.zcc.highmyopia.po;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("patient_vision_records")
public class PatientVisionRecords implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;  // 自增主键

    private String patientName;  // 患者姓名
    private String visitNumber;  // 就诊号
    private String patientId;  // 患者ID

    private String scdOs;  // 左眼裸眼视力
    private String scdOd;  // 右眼裸眼视力
    private String scdOsValue;  // 左眼裸眼视力值
    private String scdOdValue;  // 右眼裸眼视力值

    private String ccdOs;  // 左眼矫正视力
    private String ccdOd;  // 右眼矫正视力
    private String ccdOsValue;  // 左眼矫正视力值
    private String ccdOdValue;  // 右眼矫正视力值

    private String iopOs;  // 左眼眼压
    private String iopOd;  // 右眼眼压

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;  // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;  // 更新时间
}
