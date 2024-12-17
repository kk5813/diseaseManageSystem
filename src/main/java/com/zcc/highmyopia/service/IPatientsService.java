package com.zcc.highmyopia.service;

import com.zcc.highmyopia.po.Patients;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aigao
 * @since 2024-12-05
 */
public interface IPatientsService extends IService<Patients> {
    /**
    * 分页查询
     * @param pageSize 一页大小
     * @param pageNumber 页号
     * @return List<Patients>
    */
    List<Patients> pageQuery(Integer pageNumber,Integer pageSize);

}
