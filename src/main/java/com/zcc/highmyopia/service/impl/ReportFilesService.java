package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.IReportFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Service
@RequiredArgsConstructor
public class ReportFilesService extends ServiceImpl<IReportFilesMapper, ReportFiles> implements IReportFilesService {

    @Resource
    private IReportFilesMapper reportFilesMapper;

    @Override
    public List<ReportFiles> getReportFilePDFById(Long id) {
        return reportFilesMapper.getReportFilePDFById(id);
    }

    @Override
    public List<ReportFiles> getReportFileByVisitNumber(String visitNumber) {
        return reportFilesMapper.getReportFileByVisitNumber(visitNumber);
    }

    @Override
    public int getDownLoadReportFileCountByVisitNumber(String visitNumber) {
        return reportFilesMapper.getDownLoadReportFileCountByVisitNumber(visitNumber);
    }
}
