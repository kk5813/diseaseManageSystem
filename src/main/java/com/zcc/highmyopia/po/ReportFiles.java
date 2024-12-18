package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
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
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("report_files")
public class ReportFiles {

    private Long id;
    private Long reportId;
    private String fileType;
    private String fileUrl;
    private Integer isDownLoad;
    private String filePath; // 确保是 String 类型
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime  createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;


}
