package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.mapper.IPatientVisionRecordsMapper;
import com.zcc.highmyopia.po.PatientVisionRecords;
import com.zcc.highmyopia.service.IPatientVisionRecordsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PatientVisionRecordsServiceImpl
 * @Description
 * @Author aigao
 * @Date 2024/12/24 11:15
 * @Version 1.0
 */
@Service
public class PatientVisionRecordsServiceImpl extends ServiceImpl<IPatientVisionRecordsMapper, PatientVisionRecords> implements IPatientVisionRecordsService {
    @Resource
    private IPatientVisionRecordsMapper PatientVisionRecordsMapper;
    @Override
    public Result pageQuery(int pageNumber, int pageSize) {
        LambdaQueryWrapper<PatientVisionRecords> PatientVisionRecordsLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<PatientVisionRecords> PatientVisionRecordsPage = new Page<>(pageNumber, pageSize);
        IPage<PatientVisionRecords> PatientVisionRecordsIPage = PatientVisionRecordsMapper.selectPage(PatientVisionRecordsPage,PatientVisionRecordsLambdaQueryWrapper);
        return Result.succ(PatientVisionRecordsIPage);
    }
}
