package com.zcc.highmyopia.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Data
public class CheckReportVO {

    private String patientId;
    private String itemCode;
    private String itemName;
    private String visitNumber;

    private String checkTime;

    List<ReportFilesVO> files;
}
