package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.ICheckReportsMapper;
import com.zcc.highmyopia.mapper.IVisitsMapper;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.Visits;
import com.zcc.highmyopia.service.ICheckReportsService;
import com.zcc.highmyopia.service.IVisitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Service
@RequiredArgsConstructor
public class CheckReportsService extends ServiceImpl<ICheckReportsMapper, CheckReports> implements ICheckReportsService {

    @Resource
    private ICheckReportsMapper checkReportsMapper;

    @Override
    public List<CheckReports> getCheckReportById(Long patientId, String visitNumber) {
        return checkReportsMapper.getCheckReportById(patientId, visitNumber);
    }
}
