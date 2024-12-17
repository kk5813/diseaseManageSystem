package com.zcc.highmyopia.service;


import com.zcc.highmyopia.common.dto.ElementDTO;
import com.zcc.highmyopia.hospital.entity.ElementEntity;
import com.zcc.highmyopia.po.Element;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
public interface IElementService{

    void addElement(ElementEntity elementEntity);

    void editElement(ElementEntity elementEntity);

    List<Element> queryElement(ElementDTO elementDto);
}
