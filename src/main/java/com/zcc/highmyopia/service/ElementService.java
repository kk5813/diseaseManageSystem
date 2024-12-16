package com.zcc.highmyopia.service;

import com.zcc.highmyopia.common.dto.MedicalRecordDTO;
import com.zcc.highmyopia.common.vo.MedicalRecordVO;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
public interface ElementService {
    MedicalRecordVO queryMedicalRecord(MedicalRecordDTO medicalRecordDTO);

    void addMedicalRecord(MedicalRecordVO medicalRecordVO);
}
