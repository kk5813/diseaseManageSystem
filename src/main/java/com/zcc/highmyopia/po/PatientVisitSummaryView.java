package com.zcc.highmyopia.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PatientVisitSummaryView {
    private String visitNumber;
    private Long elementId;
    private Long patientId;
    private String mainAppeal;
    private String pastHistory;
    private String presentIllness;
    private String allergy;
    private String specialOs;
    private String specialOd;
    private String physicalExam;
    private String dispose;
    private String diagTime;
    private String diagName;
    private String diagCode;
    private String scdOs;
    private String scdOd;
    private String scdOsValue;
    private String scdOdValue;
    private String ccdOs;
    private String ccdOd;
    private String ccdOsValue;
    private String ccdOdValue;
    private String iopOs;
    private String iopOd;
}
