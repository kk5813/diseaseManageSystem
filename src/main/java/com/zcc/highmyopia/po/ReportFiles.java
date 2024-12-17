package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    private Short isDownLoad;
    private String filePath;

    private Date createTime;
    private Date updateTime;
}
