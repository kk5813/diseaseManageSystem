package com.zcc.highmyopia.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description 电子病历VO对象,可以继续补充信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordVO implements Serializable {

    private String patientName;

    private String sexName;

    private Integer age;
}
