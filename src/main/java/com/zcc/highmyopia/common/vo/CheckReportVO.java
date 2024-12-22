package com.zcc.highmyopia.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckReportVO {

    private String patientId;
    private String itemCode;
    private String itemName;
    private String visitNumber;

    private String checkTime;

    List<ReportFilesVO> files;
}
