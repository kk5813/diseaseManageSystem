package com.zcc.highmyopia.common.dto;

import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName AIModelResultDTO
 * @Description
 * @Author aigao
 * @Date 2024/12/30 23:53
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckReportDTO {

    private CheckReports checkReports;

    private List<ReportFiles> reportFiles;

}
