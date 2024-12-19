package com.zcc.highmyopia.hospital.entity;

import com.zcc.highmyopia.hospital.repository.impl.SaveRepository;
import com.zcc.highmyopia.po.Element;
import com.zcc.highmyopia.po.Patients;
import javafx.scene.input.DataFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientEntity {

    private String tel1;

    private String birthday;

    private Integer sex;

    private String sexName;

    private String name;

    private Long id;

    private String idNumber;

    public static Patients entityToPo(PatientEntity patientEntity) {
        Patients patients = new Patients();
        patients.setId(patientEntity.getId());
        patients.setName(patientEntity.getName());
        patients.setPhone(patientEntity.getTel1());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        patients.setBirthday(LocalDateTime.parse(patientEntity.getBirthday(), formatter));
        patients.setSexName(patientEntity.getSexName());
        patients.setStatus(1);
        patients.setIdNumber(patientEntity.getIdNumber());
        return patients;
    }
}
