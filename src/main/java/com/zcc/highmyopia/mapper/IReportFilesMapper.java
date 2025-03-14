package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.ReportFiles;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Mapper
@Component
public interface IReportFilesMapper extends BaseMapper<ReportFiles> {
    void insertBatch(List<ReportFiles> files);

    List<ReportFiles> getNotDownLoad();

    List<ReportFiles> getNotDownLoadByVisitNumber(String visitNumber);

    List<ReportFiles> getNotDownLoadByPatientID(Long patientID);

    void updateReportFiles(ReportFiles reportFile);

    List<ReportFiles> queryBatch(Long reportId);

    List<ReportFiles> getReportFilePDFById(Long id);

    List<ReportFiles> getReportFile(Long id);

    List<ReportFiles> getReportFileByReportID(Long reportId);

    List<ReportFiles> getReportFileByVisitNumber(String visitNumber);
}
