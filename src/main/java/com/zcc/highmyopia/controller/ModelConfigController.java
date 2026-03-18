package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.mapper.IModelDiseaseMapper;
import com.zcc.highmyopia.mapper.IModelLineMapper;
import com.zcc.highmyopia.mapper.IModelNodeMapper;
import com.zcc.highmyopia.po.ModelDisease;
import com.zcc.highmyopia.po.ModelLine;
import com.zcc.highmyopia.po.ModelNode;
import com.zcc.highmyopia.service.IModelDiseaseService;
import com.zcc.highmyopia.service.IModelLineService;
import com.zcc.highmyopia.service.IModelNodeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "模型节点管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/config")
public class ModelConfigController {

    @Autowired
    IModelDiseaseService modelDiseaseService;
    @Autowired
    IModelLineService modelLineService;
    @Autowired
    IModelNodeService modelNodeService;

    @Autowired
    IModelLineMapper modelLineMapper;
    @Autowired
    IModelNodeMapper modelNodeMapper;
    @Autowired
    IModelDiseaseMapper modelDiseaseMapper;

    @GetMapping("get_disease")
    @RequiresAuthentication
    public Result getModelDisease(){
        List<ModelDisease> list = modelDiseaseService.list();
        return Result.succ(list);
    }
    @GetMapping("get_node")
    @RequiresAuthentication
    public Result getModelNode(){
        List<ModelNode> list = modelNodeService.list();
        return Result.succ(list);
    }
    @GetMapping("get_line")
    @RequiresAuthentication
    public Result getModelLine(){
        List<ModelLine> list = modelLineService.list();
        return Result.succ(list);
    }
    @DeleteMapping("del_disease/{diseaseId}")
    @RequiresAuthentication
    public Result delModelDisease(@PathVariable("diseaseId") Integer diseaseId){
        int i = modelDiseaseMapper.deleteById(diseaseId);
        return i > 0 ? Result.succ(null) : Result.fail(null);
    }
    @DeleteMapping("del_node/{nodeId}")
    @RequiresAuthentication
    public Result delModelNode(@PathVariable("nodeId") Integer nodeId){
        int i = modelNodeMapper.deleteById(nodeId);
        return i > 0 ? Result.succ(null) : Result.fail(null);
    }
    @DeleteMapping("del_line/{lineId}")
    @RequiresAuthentication
    public Result delModelLine(@PathVariable("lineId") Integer lineId){
        int i = modelLineMapper.deleteById(lineId);
        return i > 0 ? Result.succ(null) : Result.fail(null);
    }
    @PostMapping("mod_disease")
    @RequiresAuthentication
    public Result modModelDisease(@RequestBody ModelDisease modelDisease){
        boolean b = modelDiseaseService.saveOrUpdate(modelDisease);
        return b ? Result.succ(null) : Result.fail(null);
    }
    @PostMapping("mod_node")
    @RequiresAuthentication
    public Result modModelNode(@RequestBody ModelNode modelNode){
        boolean b = modelNodeService.saveOrUpdate(modelNode);
        return b ? Result.succ(null) : Result.fail(null);
    }
    @PostMapping("mod_line")
    @RequiresAuthentication
    public Result modModelLine(@RequestBody ModelLine modelLine){
        boolean b = modelLineService.saveOrUpdate(modelLine);
        return b ? Result.succ(null) : Result.fail(null);
    }
    @PostMapping("add_disease")
    @RequiresAuthentication
    public Result addModelDisease(@RequestBody ModelDisease modelDisease){
        boolean b = modelDiseaseService.saveOrUpdate(modelDisease);
        return b ? Result.succ(null) : Result.fail(null);
    }
    @PostMapping("add_node")
    @RequiresAuthentication
    public Result addModelNode(@RequestBody ModelNode modelNode){
        boolean b = modelNodeService.saveOrUpdate(modelNode);
        return b ? Result.succ(null) : Result.fail(null);
    }
    @PostMapping("add_line")
    @RequiresAuthentication
    public Result addModelLine(@RequestBody ModelLine modelLine){
        boolean b = modelLineService.saveOrUpdate(modelLine);
        return b ? Result.succ(null) : Result.fail(null);
    }

}
