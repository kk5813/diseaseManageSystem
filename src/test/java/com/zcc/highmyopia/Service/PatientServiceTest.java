package com.zcc.highmyopia.Service;

import com.zcc.highmyopia.entity.Patients;
import com.zcc.highmyopia.service.old.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @ClassName PatientServiceTest
 * @Description
 * @Author aigao
 * @Date 2024/12/5 16:44
 * @Version 1.0
 */
@SpringBootTest
public class PatientServiceTest {
    @Autowired
    PatientService patientService;
    @Test
    void pageQuery(){
        List<Patients> patients = patientService.pageQuery(2, 1);
        patients.forEach(System.out::println);
    }
}