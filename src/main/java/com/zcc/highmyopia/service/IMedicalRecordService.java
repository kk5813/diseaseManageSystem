package com.zcc.highmyopia.service;

import com.zcc.highmyopia.dto.MedicalRecordDTO;
import com.zcc.highmyopia.vo.MedicalRecordVO;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
public interface IMedicalRecordService {
    MedicalRecordVO queryMedicalRecord(MedicalRecordDTO medicalRecordDTO);

    void addMedicalRecord(MedicalRecordVO medicalRecordVO);
}
