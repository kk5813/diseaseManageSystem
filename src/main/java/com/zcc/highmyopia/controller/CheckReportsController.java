package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.CheckReportVO;
import com.zcc.highmyopia.common.vo.ReportFilesVO;
import com.zcc.highmyopia.mapper.ICheckReportsMapper;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.CheckResults;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.ICheckReportsService;
import com.zcc.highmyopia.service.ICheckResultsService;
import com.zcc.highmyopia.service.IReportFilesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "检测报告管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/check_reports")
public class CheckReportsController {

    private final ICheckReportsService checkReportsService;
    private final IReportFilesService reportFilesService;
    private final IReportFilesMapper reportFilesMapper;

    @GetMapping("find/{patientId}")
    @ApiOperation(value = "获取患者检查报告")
    @RequiresAuthentication
    public Result findVisitsByPatientId(@PathVariable(name = "patientId") Long patientId){
        // 查询符合 patientId 的所有记录
        List<CheckReports> list = checkReportsService.list(
                new LambdaQueryWrapper<CheckReports>()
                        .eq(CheckReports::getPatientId, patientId)
        );
        List<CheckReportVO> checkReportVOS = list.stream()
                .map(checkReport -> {
                    Long reportId = checkReport.getId();
                    CheckReportVO checkReportVO = new CheckReportVO();
                    checkReportVO.setPatientId(String.valueOf(patientId));
                    checkReportVO.setItemName(checkReport.getItemName());
                    checkReportVO.setItemCode(checkReport.getItemCode());
                    checkReportVO.setVisitNumber(checkReport.getVisitNumber());
                    checkReportVO.setCheckTime(String.valueOf(checkReport.getCheckTime()));
                    // 查询文件
//                    List<ReportFiles> reportFilesList = reportFilesService.list(new LambdaQueryWrapper<ReportFiles>()
//                                    .eq(ReportFiles::getReportId, reportId));
                    List<ReportFiles> reportFilesList = reportFilesMapper.queryBatch(reportId);

                    List<ReportFilesVO> reportFilesVOS = reportFilesList.stream()
                            .map(reportFile -> {
                                ReportFilesVO reportFilesVO = new ReportFilesVO();
                                reportFilesVO.setType(reportFile.getFileType());
                                reportFilesVO.setFilePath(reportFile.getFilePath());
                                return reportFilesVO;
                            })
                            .collect(Collectors.toList());
                    checkReportVO.setFiles(reportFilesVOS);
                    return checkReportVO;
                })
                .collect(Collectors.toList());
        return Result.succ(checkReportVOS);
    }

}
