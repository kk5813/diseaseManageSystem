package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.AI.repository.DiagnoseRepository;
import com.zcc.highmyopia.AI.repository.IDiagnoseRepository;
import com.zcc.highmyopia.AI.service.tree.factory.engine.impl.DecisionTreeEngine;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.dto.CategoryCountDTO;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.CategoryGroupCountVO;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.hospital.service.IGetDataService;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.Doctor;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.ICheckReportsService;
import com.zcc.highmyopia.service.IVisitsService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.zcc.highmyopia.controller.CheckReportsController.LocalPathToVirtualPath;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "当天患者查询管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/today")
public class TodayController {


    private final ICheckReportsService checkReportsService;
    private final IDownLoadService downLoadService;
    private final IVisitsService visitsService;
    private final IGetDataService getDataService;
    private final IDiagnoseRepository diagnoseRepository;

    @Value("${hospital.filePath}")
    private String filePath;

    /**
     * @Description 此方法为当天来的患者进行第三方库表查询
     * @param patientId,1874356694409191426
     */
    @GetMapping("onlySearch/{patientId}")
    @ApiOperation(value = "当天来的患者进行第三方库表查询")
    @RequiresAuthentication
    public Result onlySearch(@PathVariable(name = "patientId") Long patientId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDateTime.now().format(formatter);
//        downLoadService.getCheckReportByPatientId(today, today, String.valueOf(patientId));
//        downLoadService.DownLoadReportImageBatch();
        // 拿到图片并返回
        List<CheckReports> checkReports =  diagnoseRepository.getCheckReport(patientId);

        // 暂时多项相同的检查换成第一个
        List<CheckReports> values = new ArrayList<>(checkReports.stream()
                .collect(Collectors.toMap(CheckReports::getItemName, checkReport -> checkReport, (existing, replacement) -> existing))
                .values());
        Map<String, String> url = new HashMap<>();
        values.forEach(e -> {
            List<ReportFiles> reportFiles = diagnoseRepository.getReportFile(e.getId());
            if (reportFiles != null && !reportFiles.isEmpty()) {
                ReportFiles reportFiles1 = reportFiles.get(0);
                String itemName = e.getItemName();
                if (reportFiles1.getType().equals("application/pdf")){
                    reportFiles1.setFilePath(CheckReportsController.pdfPathToImgPath(reportFiles1.getUrl()));
                    reportFiles1.setType("image/png");
                }
                url.put(itemName, GetRelativePath(reportFiles1.getFilePath()));
            }
        });
        System.out.println(url);
        return Result.succ(url);
    }

    @PostMapping("CategoryCount")
    @ApiOperation(value = "分门别类患病人数统计")
    @RequiresAuthentication
    public Result categoryCount(@RequestBody CategoryCountDTO categoryCountDTO){
        log.info("收到请求");
        List<CategoryGroupCountVO> categoryGroupCountVOList = visitsService.categoryCount(categoryCountDTO);
        return Result.succ(categoryGroupCountVOList);
    }

    @GetMapping("get_test_data")
    @ApiOperation(value = "部署服务器上拉取数据使用")
    @RequiresAuthentication
    public Result getTestData(@RequestParam String beginData, @RequestParam String endData){
        getDataService.getDataTest(beginData,endData);
        return Result.succ(null);
    }

    public String GetRelativePath(String filePath_absolute){
        Path absolute = Paths.get(filePath_absolute);  // "E:/xxx/xxx/xxx"
        Path relative = Paths.get(filePath);          // "E:/xxx/"

        return String.valueOf(relative.relativize(absolute));
    }
}
