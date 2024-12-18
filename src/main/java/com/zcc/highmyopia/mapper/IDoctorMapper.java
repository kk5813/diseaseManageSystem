package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.po.Doctor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Mapper
@Component
public interface IDoctorMapper {
    Doctor getDoctorIdByName(String doctorName);
}
