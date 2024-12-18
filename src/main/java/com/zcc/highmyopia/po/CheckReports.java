package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("check_reports")
public class CheckReports implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;  // 报告表ID

    private Long patientId;  // 患者ID
    private String visitNumber;  // 就诊ID

    private String itemCode;  // 检查项目编码
    private String itemName;  // 检查项目名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkTime;  // 检查时间
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}