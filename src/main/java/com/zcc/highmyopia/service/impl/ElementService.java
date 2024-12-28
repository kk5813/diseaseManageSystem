package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.dto.ElementDTO;
import com.zcc.highmyopia.common.dto.ElementShowDTO;
import com.zcc.highmyopia.hospital.entity.ElementEntity;
import com.zcc.highmyopia.mapper.IElementMapper;
import com.zcc.highmyopia.po.Element;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.service.IElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
@Service
@RequiredArgsConstructor
public class ElementService extends ServiceImpl<IElementMapper, Element> implements IElementService {

    private final IElementMapper elementMapper;

    // 新增
    @Override
    public void addElement(ElementEntity elementEntity) {

        Element element = ElementEntity.entityToPo(elementEntity);
        elementMapper.insert(element);

    }

    @Override
    public void editElement(ElementEntity elementEntity) {
        Element element = ElementEntity.entityToPo(elementEntity);
        LambdaUpdateWrapper<Element> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Element::getId, element.getId());
        wrapper.set(element.getPatientId() != null, Element::getPatientId, element.getPatientId());
        wrapper.set(element.getPastHistory() != null, Element::getPastHistory, element.getPastHistory());
        wrapper.set(element.getDispose() != null, Element::getDispose, element.getDispose());
        wrapper.set(element.getMainAppeal() != null, Element::getMainAppeal, element.getMainAppeal());
        wrapper.set(element.getAllergy() != null, Element::getAllergy, element.getAllergy());
        wrapper.set(element.getSpecialOd() != null, Element::getSpecialOd, element.getSpecialOd());
        wrapper.set(element.getSpecialOs() != null, Element::getSpecialOs, element.getSpecialOs());
        wrapper.set(element.getPhysicalExam() != null, Element::getPhysicalExam, element.getPhysicalExam());
        wrapper.set(element.getVisitNumber() != null, Element::getVisitNumber, element.getVisitNumber());
        wrapper.set(element.getPresentIllness() != null, Element::getPresentIllness, element.getPresentIllness());
        wrapper.set(Element::getUpdateTime, new Date());
        elementMapper.update(element, wrapper);
    }

    @Override
    public List<Element> queryElement(ElementDTO elementDto) {
        return elementMapper.queryElementOnCondition(elementDto);
    }

    @Override
    public Element findElement(Long elementId) {
        LambdaQueryWrapper<Element> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Element::getId, elementId);
        return elementMapper.selectOne(wrapper);
    }

    @Override
    public List<Element> pageQuery(int pageNumber, int pageSize) {
        Page<Element> page = new Page<>(pageNumber, pageSize);
        IPage<Element> pages = this.page(page);
        return pages.getRecords();
    }
}
