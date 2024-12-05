package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.entity.Patients;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangyue
 * @since 2021-02-05
 */
@Mapper
@Component
public interface PatientMapper{

    @Select("select * from patient where patient_id = #{id}")
    Patients selectPatientByPId(@Param("id") String id);

    @Select("Select telephone from patient where patient_id = #{id}")
    String selectTelephoneByPatientId(@Param("id") String id);
}
