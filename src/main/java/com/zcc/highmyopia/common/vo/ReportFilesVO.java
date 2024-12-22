package com.zcc.highmyopia.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportFilesVO {

    private String type;

    private String filePath;

}
