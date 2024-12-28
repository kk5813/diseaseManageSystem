package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.ElementVO;
import com.zcc.highmyopia.mapper.IPatientVisionRecordsMapper;
import com.zcc.highmyopia.po.Element;
import com.zcc.highmyopia.po.PatientVisionRecords;
import com.zcc.highmyopia.service.IPatientVisionRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName PatientVisionRecordsController
 * @Description
 * @Author aigao
 * @Date 2024/12/24 11:17
 * @Version 1.0
 */
@RestController
@Slf4j
@Api(tags = "视力眼压管理")
@RequestMapping("/api/${app.config.api-version}/visionRecords")
public class PatientVisionRecordsController {
    @Autowired
    IPatientVisionRecordsService patientVisionRecordsService;

    @Autowired
    IPatientVisionRecordsMapper patientVisionRecordsMapper;
    @ApiOperation(value = "视力眼压信息分页查询")
    @RequiresAuthentication
    @GetMapping("page")
    public Result pageElement(@RequestParam(defaultValue = "1") int pageNumber,  // 页码默认 0
                              @RequestParam(defaultValue = "10") int pageSize) {  // 每页大小默认 10
        return patientVisionRecordsService.pageQuery(pageNumber, pageSize);
    }
}
