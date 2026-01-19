package com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcc.highmyopia.AIDiagnose.entity.request.ModelRequestVO;
import com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.FileFilter;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.po.ReportFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zcc.highmyopia.AIDiagnose.manager.utils.HttpPOST;

/**
 * @ClassName FundusFileFilterImpl
 * @Description
 * @Author aigao
 * @Date 2026/1/17 15:55
 * @Version 1.0
 */
@Component
@Slf4j
public class FundusFileFilterImpl implements FileFilter {

    @Override
    public List<String> filterFiles(List<ReportFiles> files, Map<String, String> config) {

        return files.stream().filter(e -> Objects.equals(e.getType(), "image/jpeg"))
                .map(ReportFiles::getFilePath)
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyType() {
        return Constants.FileFilterType.Fund.getType();
    }

}
