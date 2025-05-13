package com.zcc.highmyopia.hospital.entity;

import com.zcc.highmyopia.po.ElementVision;
import lombok.*;

/**
 * @Author zcc
 * @Date 2024/12/28
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ElementVisionEntity {

    private String visitNumber;  // 就诊号
    private String patientId;  // 患者ID
    private String patientName;

    private String scdOs;  // 左眼裸眼视力
    private String scdOd;  // 右眼裸眼视力
    private String scdOsValue;  // 左眼裸眼视力值
    private String scdOdValue;  // 右眼裸眼视力值

    private String ccdOs;  // 左眼矫正视力
    private String ccdOd;  // 右眼矫正视力
    private String ccdOsValue;  // 左眼矫正视力值
    private String ccdOdValue;  // 右眼矫正视力值

    private String iopOs;  // 左眼眼压
    private String iopOd;  // 右眼眼压

    public static ElementVision entityToPo(ElementVisionEntity elementVisionEntity){
        ElementVision elementVision = new ElementVision();
         elementVision.setVisitNumber(elementVisionEntity.getVisitNumber());
         elementVision.setPatientId(elementVisionEntity.getPatientId());
         elementVision.setScdOs(elementVisionEntity.getScdOs());
         elementVision.setScdOd(elementVisionEntity.getScdOd());
         elementVision.setScdOsValue(elementVisionEntity.getScdOsValue());
         elementVision.setScdOdValue(elementVisionEntity.getScdOdValue());
         elementVision.setCcdOs(elementVisionEntity.getCcdOs());
         elementVision.setCcdOd(elementVisionEntity.getCcdOd());
         elementVision.setCcdOsValue(elementVisionEntity.getCcdOsValue());
         elementVision.setCcdOdValue(elementVisionEntity.getCcdOdValue());
         elementVision.setIopOs(elementVisionEntity.getIopOs());
         elementVision.setIopOd(elementVisionEntity.getIopOd());
        return elementVision;
    }

    public static ElementVisionEntity poToEntity(ElementVision elementVision, String patientName) {
         ElementVisionEntity elementVisionEntity = ElementVisionEntity.builder()
                  .visitNumber(elementVision.getVisitNumber())
                  .patientId(elementVision.getPatientId())
                  .patientName(patientName)
                  .scdOs(elementVision.getScdOs())
                  .scdOd(elementVision.getScdOd())
                  .scdOsValue(elementVision.getScdOsValue())
                  .scdOdValue(elementVision.getScdOdValue())
                  .ccdOs(elementVision.getCcdOs())
                  .ccdOd(elementVision.getCcdOd())
                  .ccdOsValue(elementVision.getCcdOsValue())
                  .ccdOdValue(elementVision.getCcdOdValue())
                  .iopOs(elementVision.getIopOs())
                  .iopOd(elementVision.getIopOd())
                  .build();

        return elementVisionEntity;
    }
}
