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

    void updateReportFiles(ReportFiles reportFile);

    List<ReportFiles> queryBatch(Long reportId);

    List<ReportFiles> getReportFileById(Long id);

    List<ReportFiles> getReportFile(Long id);
}
