package com.zcc.highmyopia.controller;


import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.mapper.IPatientsMapper;
import com.zcc.highmyopia.service.IPatientsService;
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
