package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
public interface IReportFilesService extends IService<ReportFiles> {
    List<ReportFiles> getReportFilePDFById(Long id);

    List<ReportFiles> getReportFileByVisitNumber(String visitNumber);
}
