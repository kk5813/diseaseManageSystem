package com.zcc.highmyopia.controller;


import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.entity.Patients;
import com.zcc.highmyopia.mapper.PatientMapper;
import com.zcc.highmyopia.service.PatientService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    PatientMapper patientMapper;

    // 测试用
    @GetMapping("/index")
    public Object index() {
        Patients patient = patientService.getById(1L);
        return Result.succ(patient);
    }

    // 患者列表
    @GetMapping("/list")
    @RequiresAuthentication
    public Result list() {
        List<Patients> patientList = patientService.list();
        return Result.succ(patientList);
    }

    //          编辑患者
    @PostMapping("/edit")
    @RequiresAuthentication
    public Result editUser( @RequestBody Patients patient) {
        patientService.saveOrUpdate(patient);
        return Result.succ(null);
    }

    //  根据patientId查询患者基本信息
    @GetMapping("/patientIndex/{id}")
    @RequiresAuthentication
    public Result patientByPatientId(@PathVariable(name = "id") String id) {
        return Result.succ(patientMapper.selectPatientByPId(id));
    }


}
