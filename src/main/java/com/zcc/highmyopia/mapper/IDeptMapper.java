package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.po.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Mapper
@Component
public interface IDeptMapper {
    Dept getDeptByName(String deptName);
}
