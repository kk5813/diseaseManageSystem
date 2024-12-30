package com.zcc.highmyopia.po;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("element_vision")
public class ElementVision implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;  // 自增主键

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

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime  createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}
