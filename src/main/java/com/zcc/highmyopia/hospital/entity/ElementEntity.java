package com.zcc.highmyopia.hospital.entity;

import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.Element;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementEntity {

    private String id;             // 病历ID
    private Long patientId;        // 患者ID
    private String patientName;    // 患者姓名
    private String mainAppeal;      // 主诉
    private String pastHistory;     // 既往史
    private String presentIllness; // 现病史
    private String allergy;        // 过敏史
    private String specialOs;      // 左眼科专科检查
    private String specialOd;      // 右眼科专科检查
    private String visitNumber;    // 就诊编号


    private String physicalExam;   // 体格检查
    private String dispose;        // 处理意见


    public static Element entityToPo(ElementEntity elementEntity) {
        Element element = new Element();
        element.setId(elementEntity.getId());
        element.setPatientId(elementEntity.getPatientId());
        element.setMainAppeal(elementEntity.getMainAppeal());
        element.setPastHistory(elementEntity.getPastHistory());
        element.setPresentIllness(elementEntity.getPresentIllness());
        element.setAllergy(elementEntity.getAllergy());
        element.setSpecialOs(elementEntity.getSpecialOs());
        element.setSpecialOd(elementEntity.getSpecialOd());
        element.setVisitNumber(elementEntity.getVisitNumber());
        element.setPhysicalExam(elementEntity.getPhysicalExam());
        element.setDispose(elementEntity.getDispose());
        return element;
    }

}
