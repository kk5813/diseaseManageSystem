package com.zcc.highmyopia.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;

import java.io.Serializable;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description 电子病历VO视图,可以继续补充信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordVO implements Serializable {

    // 病历ID 没传过来
    // private String id;

    private String visitNumber;

    private String patientId;

    private String patientName;

    private String scdOS;

    private String scdOD;

    private String mainAppeal;

    private String presentIllness;

    private String pastHistory;

    private String physicalExam;

    private String dispose;

    private String allergy;

    private String specialOD;

    private String specialOS;

}

