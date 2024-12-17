package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.dto.ElementDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.ElementVO;
import com.zcc.highmyopia.hospital.entity.ElementEntity;
import com.zcc.highmyopia.po.Element;
import com.zcc.highmyopia.service.IElementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/12
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/${app.config.api-version}/element")
public class ElementController {

    @Autowired
    private IElementService elementService;


    @PostMapping("add")
    public Result addElement(@RequestBody ElementEntity elementEntity){
        elementService.addElement(elementEntity);
        return Result.succ(null);
    }

    @PutMapping("edit")
    public Result editElement(@RequestBody ElementEntity elementEntity){
        elementService.editElement(elementEntity);
        return Result.succ("病历更新成功");
    }


    // 条件查询
    @GetMapping("search")
    public Result queryElement(@RequestBody ElementDTO elementDto){
        List<Element> elements = elementService.queryElement(elementDto);
        Long total = (long) elements.size();
        ElementVO elementVO = new ElementVO();
        elementVO.setElements(elements);
        elementVO.setTotal(total);
        return Result.succ("维护成功", elements);
    }
}
