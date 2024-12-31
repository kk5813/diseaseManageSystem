//package com.zcc.highmyopia.controller;
//
//import com.zcc.highmyopia.common.dto.ElementDTO;
//import com.zcc.highmyopia.common.dto.ElementShowDTO;
//import com.zcc.highmyopia.common.lang.Result;
//import com.zcc.highmyopia.common.vo.ElementVO;
//import com.zcc.highmyopia.common.vo.PatientsVO;
//import com.zcc.highmyopia.hospital.entity.ElementEntity;
//import com.zcc.highmyopia.mapper.IElementMapper;
//import com.zcc.highmyopia.po.Element;
//import com.zcc.highmyopia.po.Patients;
//import com.zcc.highmyopia.service.IElementService;
//import com.zcc.highmyopia.service.IPatientsService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.authz.annotation.RequiresAuthentication;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author zcc
// * @Date 2024/12/12
// * @Description
// */
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//@Api(tags = "病例报告管理")
//@RequestMapping("/api/${app.config.api-version}/element")
//public class ElementController {
//
//    @Autowired
//    private IElementService elementService;
//
//    @Resource
//    private IPatientsService patientsService;
//
//
//    @PostMapping("add")
//    @RequiresAuthentication
//    @ApiOperation(value = "添加病例报告")
//    public Result addElement(@RequestBody ElementEntity elementEntity){
//        elementService.addElement(elementEntity);
//        return Result.succ("病历添加成功");
//    }
//
//    @PutMapping("edit")
//    @RequiresAuthentication
//    @ApiOperation(value = "编辑病例报告")
//    public Result editElement(@RequestBody ElementEntity elementEntity){
//        elementService.editElement(elementEntity);
//        return Result.succ("病历更新成功");
//    }
//
////    @PostMapping("search")
////    @RequiresAuthentication
////    @ApiOperation(value = "条件查询病例报告")
////    public Result queryElement(@RequestBody ElementDTO elementDto){
//////        List<Element> elements = elementService.queryElement(elementDto);
////        List<Element> elements = elementService.queryElement(elementDto);
////        List<ElementEntity> elementEntities = new ArrayList<>();
////        for (Element element : elements) {
////            String patientName = patientsService.getPatientById(element.getPatientId()).getName();
////            ElementEntity elementEntity = ElementEntity.poToEntity(element,patientName);
////            elementEntities.add(elementEntity);
////        }
////        Long total = (long) elements.size();
////        ElementVO elementVO = new ElementVO();
////        elementVO.setElements(elementEntities);
////        elementVO.setTotal(total);
////        return Result.succ("维护成功", elementVO);
////    }
////
////    @GetMapping("find/{elementId}")
////    @RequiresAuthentication
////    @ApiOperation(value = "ID查询病例报告")
////    public Result findElement(@PathVariable(name = "elementId") Long elementId){
////        Element elements = elementService.findElement(elementId);
////        String patientName = patientsService.getPatientById(elements.getPatientId()).getName();
////        ElementEntity elementEntity = ElementEntity.poToEntity(elements,patientName);
////        return Result.succ(elementEntity);
////    }
////
////    @GetMapping("page")
////    @RequiresAuthentication
////    @ApiOperation(value = "分页查询病例报告")
////    public Result pageElement(@RequestParam(defaultValue = "1") int pageNumber,  // 页码默认 0
////                              @RequestParam(defaultValue = "10") int pageSize) {  // 每页大小默认 10
////        List<Element> elements = elementService.pageQuery(pageNumber, pageSize);
////        List<ElementEntity> elementEntities = new ArrayList<>();
////        for (Element element : elements) {
////            String patientName = patientsService.getPatientById(element.getPatientId()).getName();
////            ElementEntity elementEntity = ElementEntity.poToEntity(element,patientName);
////            elementEntities.add(elementEntity);
////        }
////        ElementVO elementVO = new ElementVO();
////        elementVO.setElements(elementEntities);
////        elementVO.setTotal((long) elements.size());
////        return Result.succ(elementVO);
////    }
//
//}
