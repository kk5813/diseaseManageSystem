package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.common.dto.ElementDTO;
import com.zcc.highmyopia.po.Element;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Mapper
@Component
public interface IElementMapper extends BaseMapper<Element> {

    List<Element> queryElementOnCondition(ElementDTO elementDto);
}
