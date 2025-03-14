package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.Doctor;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
public interface IDoctorService extends IService<Doctor> {

    Doctor getDoctorById(Long doctorId);

}
