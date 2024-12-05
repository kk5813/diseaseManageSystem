package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.entity.Patients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName PatientMapperTest
 * @Description
 * @Author aigao
 * @Date 2024/12/5 15:46
 * @Version 1.0
 */
@SpringBootTest
public class PatientMapperTest {
    @Autowired
    PatientMapper patientMapper;
    @Test
    void selectPatientByID(){
        Patients patients = patientMapper.selectPatientByPId("1");
        System.out.println(patients);
    }
    @Test
    void updatePatients(){
        Patients patients = new Patients();
        patients.setId(1L);
        patients.setPhone("110011");
        System.out.println(patientMapper.updatePatients(patients));
        Patients patients1 = patientMapper.selectPatientByPId("1");
        System.out.println(patients1);
    }
}
