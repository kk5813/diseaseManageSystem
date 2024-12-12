package com.zcc.highmyopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordDTO {

    private String patientId;

    private Date dataStart;

    private Date dataEnd;

    private String patientName;

    private String diagnosisType;
}
