package com.zcc.highmyopia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcc.highmyopia.common.dto.CheckReportDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.CheckReportVO;
import com.zcc.highmyopia.common.vo.ReportFilesVO;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.ICheckReportsService;
import com.zcc.highmyopia.service.IReportFilesService;
import com.zcc.highmyopia.util.PDFToImg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Value("${hospital.pdf2ImgPath}")
    private static String PDFToImgRelativePath;

    @Value("${hospital.localImage}")
    private static String ImagePathLocalHost;

    @Value("${hospital.filePath}")
    private String targetPath;


    // todo 修改
    @GetMapping("find/{patientId}")
    @ApiOperation(value = "获取患者检查报告")
    @RequiresAuthentication
    public Result findVisitsByPatientId(@PathVariable(name = "patientId" ) Long patientId,
                                        @RequestParam(defaultValue = "") String startTime ,
                                        @RequestParam(defaultValue = "") String endTime){
        // 查询符合 patientId startTime  endTime 的所有记录
        LambdaQueryWrapper<CheckReports> checkReportsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        checkReportsLambdaQueryWrapper.eq(CheckReports::getPatientId, patientId);
        if(StringUtils.isNotEmpty(startTime)){
            checkReportsLambdaQueryWrapper.ge(CheckReports::getCheckTime, startTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            checkReportsLambdaQueryWrapper.le(CheckReports::getCheckTime,endTime);
        }
        List<CheckReports> list = checkReportsService.list( checkReportsLambdaQueryWrapper);
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
                    List<ReportFiles> reportFilesList = reportFilesMapper.getReportFileByReportID(reportId);
                    if (reportFilesList == null || reportFilesList.isEmpty()) return checkReportVO;
                    List<ReportFilesVO> reportFilesVOS = reportFilesList.stream()
                            .map(reportFile -> {
                                ReportFilesVO reportFilesVO = new ReportFilesVO();
                                reportFilesVO.setType(reportFile.getType());
                                reportFilesVO.setFilePath(LocalPathToVirtualPath(reportFile.getFilePath()));
                                return reportFilesVO;
                            })
                            .collect(Collectors.toList());
                    checkReportVO.setFiles(reportFilesVOS);
                    return checkReportVO;
                })
                .collect(Collectors.toList());
        return Result.succ(checkReportVOS);
    }

    @GetMapping("reportFiles")
    @ApiOperation(value = "病历时间线里获取患者检查报告")
    @RequiresAuthentication
    public Result findVisitsByPatientId(@RequestParam String patientId,
                                        @RequestParam String visitNumber){
        log.info("参数为patientId:{}, visitNumber:{}", patientId, visitNumber);
        List<CheckReports> checkReportsList = checkReportsService.getCheckReportById(Long.valueOf(patientId), visitNumber);
        if (checkReportsList == null || checkReportsList.isEmpty()) return Result.succ(null);
        List<CheckReportDTO> collect = checkReportsList.stream().map(
                e -> {
                    List<ReportFiles> reportFilesList = reportFilesService.getReportFilePDFById(e.getId());
//                    reportFilesList.forEach(
//                            c -> {
//                                if (c.getFilePath() != null){
//                                    String newPath = pdfPathToImgPath(c.getFilePath());
//                                    c.setFilePath(newPath);
//                                    c.setType("image/png");
//                                }
//                            }
//                    );
                    return CheckReportDTO.builder()
                            .checkReports(e)
                            .reportFiles(reportFilesList)
                            .build();
                }
        ).collect(Collectors.toList());
        log.info("{}", collect);
        return Result.succ(collect);
    }


    static String pdfPathToImgPath(String filePath){
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        int dot = file.getName().lastIndexOf(".");
        // todo 这里图片格式写死了为png, 不建议搜索文件，这样太慢了，要改的话不如直接改数据库，不改变原来的表而是新加一个表，来做pdf报告映射的png路径，这样修改最小。
        //Path path = Paths.get(parentFile.getPath() + PDFToImgRelativePath, file.getName().substring(0, dot) + PDFToImg.PNG);
        Path path = Paths.get(ImagePathLocalHost + PDFToImgRelativePath,file.getName().substring(0,dot) + PDFToImg.PNG);
        log.info(path.toString());
        return path.toString();
    }
    public String LocalPathToVirtualPath(String filePathA){
//        File file = new File(filePath);
//        return Paths.get(ImagePathLocalHost,file.getName()).toString();
        // 将全局根目录和文件绝对路径转换为Path对象
        Path rootPath = Paths.get(targetPath).toAbsolutePath().normalize();
        Path filePath = Paths.get(filePathA).toAbsolutePath().normalize();

        // 计算相对路径
        Path relativePath = rootPath.relativize(filePath);
        return relativePath.toString();
    }


}
