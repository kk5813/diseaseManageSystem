package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IModelNodeMapper;
import com.zcc.highmyopia.po.ModelNode;
import com.zcc.highmyopia.service.IModelNodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Service
public class ModelNodeService extends ServiceImpl<IModelNodeMapper, ModelNode> implements IModelNodeService {

    @Resource
    private IModelNodeMapper modelNodeMapper;

    @Override
    public List<ModelNode> getModelNodeByDiseaseId(Integer diseaseId) {
        return modelNodeMapper.selectList(new LambdaQueryWrapper<ModelNode>()
                .eq(ModelNode::getDiseaseId, diseaseId));
    }
}
