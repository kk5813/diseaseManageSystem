package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.ModelLine;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Mapper
public interface IModelLineMapper extends BaseMapper<ModelLine> {
}
