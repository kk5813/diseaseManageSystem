package com.zcc.highmyopia.service.impl;

import com.zcc.highmyopia.common.dto.MedicalRecordDTO;
import com.zcc.highmyopia.common.vo.MedicalRecordVO;
import org.springframework.stereotype.Service;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
@Service
public class ElementService implements com.zcc.highmyopia.service.ElementService {


    @Override
    public MedicalRecordVO queryMedicalRecord(MedicalRecordDTO medicalRecordDTO) {
        MedicalRecordVO medicalRecordVO = new MedicalRecordVO();
        // 查询数据库
        // 拼装
        // 返回
        return medicalRecordVO;

    }

    @Override
    public void addMedicalRecord(MedicalRecordVO medicalRecordVO) {
        // 校验

        // 拆分封装并存储数据库中

    }
}
