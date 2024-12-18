package com.zcc.highmyopia.service;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.common.dto.ElementDTO;
import com.zcc.highmyopia.hospital.entity.ElementEntity;
import com.zcc.highmyopia.po.Element;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
public interface IElementService extends IService<Element> {

    void addElement(ElementEntity elementEntity);

    void editElement(ElementEntity elementEntity);

    List<Element> queryElement(ElementDTO elementDto);

    Element findElement(Long elementId);

    List<Element> pageQuery(int pageNumber, int pageSize);
}
