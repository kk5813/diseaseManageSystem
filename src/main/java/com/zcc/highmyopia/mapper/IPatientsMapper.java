package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.common.dto.PatientsDTO;
import com.zcc.highmyopia.po.Patients;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

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
public interface IPatientsMapper extends BaseMapper<Patients> {

    Patients selectPatientByPId(@Param("id") String id);

    int updatePatients(Patients patients);

    int batchInsert(@Param("list") List<Patients> list);

    @Select("Select phone from patients where id = #{id}")
    String selectTelephoneByPatientId(@Param("id") String id);

    List<Patients> searchPatients(PatientsDTO patientsDTO);
}
