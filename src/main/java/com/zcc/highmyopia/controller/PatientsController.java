package com.zcc.highmyopia.controller;


import com.esotericsoftware.minlog.Log;
import com.zcc.highmyopia.common.dto.PatientsDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.PatientsVO;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.mapper.IPatientsMapper;
import com.zcc.highmyopia.service.IPatientsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

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
@RequestMapping("/api/${app.config.api-version}/patients")
public class PatientsController {

    @Autowired
    IPatientsService patientService;

    @Autowired
    IPatientsMapper patientMapper;


    // 患者列表
    @GetMapping("/list")
    @RequiresAuthentication
    public Result list() {
        List<Patients> patientList = patientService.list();
        PatientsVO patientsVO = new PatientsVO();
        patientsVO.setPatients(patientList);
        patientsVO.setTotal((long) patientList.size());
        return Result.succ(patientsVO);
    }

    //  编辑患者
    @PostMapping("/edit")
    @RequiresAuthentication
    public Result editUser(@RequestBody Patients patient) {
        patientService.saveOrUpdate(patient);
        return Result.succ(null);
    }

    //  根据patientId查询患者基本信息
    @GetMapping("/find/{patientId}")
    @RequiresAuthentication
    public Result patientByPatientId(@PathVariable(name = "patientId") String patientId) {
        Log.info("精准查询用户");
        return Result.succ(patientMapper.selectPatientByPId(patientId));
    }

    //  分页查询
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

    // 模糊查找
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




}
