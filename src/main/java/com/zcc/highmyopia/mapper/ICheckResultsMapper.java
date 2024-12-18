package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.po.CheckResults;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Mapper
@Component
public interface ICheckResultsMapper {
    void insert(CheckResults checkResult);
}
