package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.mapper.IElementVisionMapper;
import com.zcc.highmyopia.po.ElementVision;
import com.zcc.highmyopia.service.IElementVisionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName PatientVisionRecordsServiceImpl
 * @Description
 * @Author aigao
 * @Date 2024/12/24 11:15
 * @Version 1.0
 */
@Service
public class ElementVisionService extends ServiceImpl<IElementVisionMapper, ElementVision> implements IElementVisionService {
    @Resource
    private IElementVisionMapper PatientVisionRecordsMapper;
    @Override
    public Result pageQuery(int pageNumber, int pageSize) {
        LambdaQueryWrapper<ElementVision> PatientVisionRecordsLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<ElementVision> PatientVisionRecordsPage = new Page<>(pageNumber, pageSize);
        IPage<ElementVision> PatientVisionRecordsIPage = PatientVisionRecordsMapper.selectPage(PatientVisionRecordsPage,PatientVisionRecordsLambdaQueryWrapper);
        return Result.succ(PatientVisionRecordsIPage);
    }
}
