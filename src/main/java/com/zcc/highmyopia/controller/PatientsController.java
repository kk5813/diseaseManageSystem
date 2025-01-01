package com.zcc.highmyopia.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.esotericsoftware.minlog.Log;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.dto.ElementShowDTO;
import com.zcc.highmyopia.common.dto.PatientsDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.PatientsVO;
import com.zcc.highmyopia.mapper.IPatientsMapper;
import com.zcc.highmyopia.po.PatientVisitSummaryView;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.service.IPatientVisitSummaryService;
import com.zcc.highmyopia.service.IPatientsService;
import com.zcc.highmyopia.service.IRedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangyue
 * @since 2021-02-05
 */
@RestController
@Slf4j
@Api(tags = "患者管理")
@RequestMapping("/api/${app.config.api-version}/patients")
public class PatientsController {

    @Autowired
    IPatientsService patientService;

    @Autowired
    IPatientsMapper patientMapper;

    @Resource
    private IRedisService redisService;

    @ApiOperation(value = "获取患者列表")
    @GetMapping("/list")
    @RequiresAuthentication
    public Result list() {
        List<Patients> patientList = patientService.list();
        PatientsVO patientsVO = new PatientsVO();
        patientsVO.setPatients(patientList);
        patientsVO.setTotal((long) patientList.size());
        return Result.succ(patientsVO);
    }

    @ApiOperation(value = " 编辑患者")
    @PostMapping("/edit")
    @RequiresAuthentication
    public Result editUser(@RequestBody Patients patient) {
        String cacheKey = Constants.RedisKey.PATIENTS + patient.getId();
        boolean saved = patientService.saveOrUpdate(patient);
        if (saved) {
            log.info("清理redis缓存{}", cacheKey);
            redisService.remove(cacheKey);
        }
        return Result.succ(null);
    }

    @ApiOperation(value = "根据patientId查询患者基本信息")
    @GetMapping("/find/{patientId}")
    @RequiresAuthentication
    public Result patientByPatientId(@PathVariable(name = "patientId") String patientId) {
        String cacheKey = Constants.RedisKey.PATIENTS + patientId;
        Patients patients = redisService.getValue(cacheKey);
        if (patients != null) return Result.succ(patients);

        patients = patientMapper.selectPatientByPId(patientId);
        redisService.setValue(cacheKey, patients);
        return Result.succ(patients);
    }

    @ApiOperation(value = "患者信息分页查询")
    @GetMapping("/page")
    @RequiresAuthentication
    public Result getPatientPage(@RequestParam(defaultValue = "1") int pageNumber,  // 页码默认 1
                               @RequestParam(defaultValue = "10") int pageSize) {  // 每页大小默认 10
        List<Patients> patients = patientService.pageQuery(pageNumber, pageSize);
        PatientsVO patientsVO = new PatientsVO();
        patientsVO.setPatients(patients);
        patientsVO.setTotal((long) patients.size());
        return Result.succ(patientsVO);
    }
    @ApiOperation(value = "患者信息模糊查找")
    @PostMapping("/search")
    @RequiresAuthentication
    public Result SearchPatients(@RequestBody PatientsDTO patientsDTO) {
        List<Patients> patients = patientService.searchPatients(patientsDTO);
        Long total = (long) patients.size();
        PatientsVO patientsVO = new PatientsVO();
        patientsVO.setPatients(patients);
        patientsVO.setTotal(total);
        return Result.succ(patientsVO);
    }

    @ApiOperation(value = "患者信息批量导出")
    @GetMapping("/export")
    @RequiresAuthentication
    public void exportCSV(HttpServletResponse response) {
        List<Patients> patients = patientService.searchPatients(new PatientsDTO());

        response.setContentType("text/csv");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try (Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "name", "sexName", "birthday", "idNumber", "phone"))) {

                // 遍历患者数据并写入 CSV
                for (Patients patient : patients) {
                    csvPrinter.printRecord(
                            patient.getId(),
                            patient.getName(),
                            patient.getSexName(),
                            patient.getBirthday(),
                            patient.getIdNumber(),
                            patient.getPhone()
                    );
                }

                csvPrinter.flush();
                byte[] fileBytes = byteArrayOutputStream.toByteArray();
                response.setHeader("Content-Disposition", "attachment; filename=\"patients.csv\"");

                // 将文件流写入响应体
                response.getOutputStream().write(fileBytes);
                response.getOutputStream().flush();
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private IPatientVisitSummaryService patientVisitSummaryService;
    @GetMapping("element_time_line")
    @RequiresAuthentication
    @ApiOperation(value = "展示病人就诊以及病历时间线")
    public Result timeLineElement(@RequestParam(defaultValue = "") String visitNumber,
                                  @RequestParam(required = false) String patientId,
                                  @RequestParam(defaultValue = "1") int pageNum,
                                  @RequestParam(defaultValue = "10") int pageSize) {
        Page<PatientVisitSummaryView> page = new Page<>(pageNum, pageSize);
        IPage<PatientVisitSummaryView> patientVisitSummaryByPage = patientVisitSummaryService.getPatientVisitSummaryByPage(visitNumber, Long.valueOf(patientId), page);
        return Result.succ(patientVisitSummaryByPage);
    }

//    @GetMapping("a")
//    @RequiresAuthentication
//    @ApiOperation(value = "病人计划随访日期时间")
//    public Result addToFollowUp(){
//        return Result.succ(null);
//    }
}
