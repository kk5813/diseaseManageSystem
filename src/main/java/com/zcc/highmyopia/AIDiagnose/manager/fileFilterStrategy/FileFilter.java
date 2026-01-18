package com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy;

import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName FileFilter
 * @Description
 * @Author aigao
 * @Date 2026/1/17 15:47
 * @Version 1.0
 */
public interface FileFilter {
    default List<String> filterFiles(List<ReportFiles> files, Map<String, String> config){
        return files.stream().map(ReportFiles::getFilePath).collect(Collectors.toList());
    }

    String getStrategyType();

}
