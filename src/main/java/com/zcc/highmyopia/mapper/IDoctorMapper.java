package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface IDoctorMapper extends BaseMapper<Doctor> {
    Doctor getDoctorIdByName(String doctorName);
}
