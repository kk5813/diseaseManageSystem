package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.dto.MedicalRecordDTO;
import com.zcc.highmyopia.service.ElementService;
import com.zcc.highmyopia.common.vo.MedicalRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/${app.config.api-version}/element")
public class ElementController {

    @Autowired
    private ElementService medicalRecordService;


    @PostMapping("add")
    public Result addMedicalRecord(@RequestBody MedicalRecordVO medicalRecordVO){

        medicalRecordService.addMedicalRecord(medicalRecordVO);

        return Result.succ("病历创建成功");
    }

    @PutMapping("edit")
    public Result editMedicalRecord(@RequestBody MedicalRecordVO medicalRecordVO){
        return Result.succ("病历更新成功");
    }
    @GetMapping("search")
    public Result queryMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO){
        // 查询电子病历并拼装
        MedicalRecordVO medicalRecordVO = medicalRecordService.queryMedicalRecord(medicalRecordDTO);

        return Result.succ(medicalRecordVO);
    }
}
