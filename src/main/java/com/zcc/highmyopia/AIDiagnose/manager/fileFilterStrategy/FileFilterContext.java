package com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy;

import com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.FileFilter;
import com.zcc.highmyopia.po.ReportFiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static cn.hutool.core.lang.Console.log;

/**
 * @ClassName FileFilterContext
 * @Description
 * @Author aigao
 * @Date 2026/1/17 15:51
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FileFilterContext {

    // 核心：Key是类型，Value是对应的策略实现
    private final Map<String, FileFilter> strategyMap = new ConcurrentHashMap<>();

    @Autowired
    public FileFilterContext(List<FileFilter> strategyList) {
        for (FileFilter strategy : strategyList) {
            // 自动注册：["OCT" -> OctFilterStrategy实例]
            strategyMap.put(strategy.getStrategyType(), strategy);
        }
    }

    // 对外暴露的统一调用方法
    public List<String> executeFilter(String type, List<ReportFiles> files, Map<String, String> config) {
        FileFilter strategy = strategyMap.get(type);
        if (strategy == null) {
            // 可以抛异常，也可以返回默认策略（比如不过滤直接返回）
            log.warn("未找到类型为 " + type + " 的文件过滤策略");
            // 不过滤直接返回
            return files.stream().map(ReportFiles::getFilePath).collect(Collectors.toList());
        }
        return strategy.filterFiles(files, config);
    }
}
