package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.dto.ElementDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.ElementVO;
import com.zcc.highmyopia.common.vo.PatientsVO;
import com.zcc.highmyopia.hospital.entity.ElementEntity;
import com.zcc.highmyopia.po.Element;
import com.zcc.highmyopia.po.Patients;
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
        return Result.succ("病历添加成功");
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
        return Result.succ("维护成功", elementVO);
    }

    // 条件查询
    @GetMapping("find/{elementId}")
    public Result findElement(@PathVariable(name = "elementId") Long elementId){
        Element elements = elementService.findElement(elementId);
        return Result.succ(elements);
    }

    // 分页查询
    @GetMapping("page")
    public Result pageElement(@RequestParam(defaultValue = "1") int pageNumber,  // 页码默认 0
                              @RequestParam(defaultValue = "10") int pageSize) {  // 每页大小默认 10
        List<Element> elements = elementService.pageQuery(pageNumber, pageSize);
        ElementVO elementVO = new ElementVO();
        elementVO.setElements(elements);
        elementVO.setTotal((long) elements.size());
        return Result.succ(elementVO);
    }
}
