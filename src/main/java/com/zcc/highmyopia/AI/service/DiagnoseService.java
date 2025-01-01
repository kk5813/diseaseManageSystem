package com.zcc.highmyopia.AI.service;

import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.AI.repository.IDiagnoseRepository;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;
import com.zcc.highmyopia.AI.service.tree.factory.engine.impl.DecisionTreeEngine;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Slf4j
@Service
public class DiagnoseService implements IDiagnoseService {

    @Resource
    private IDiagnoseRepository diagnoseRepository;

    @Resource
    private DefaultTreeFactory treeFactory;

    @Override
    public List<DiagnoseResultEntity> diagnose(DiagnoseEntity diagnoseEntity) {
        Long patientId = Long.valueOf(diagnoseEntity.getPatientId());
        List<CheckReports> checkReports =  diagnoseRepository.getCheckReport(patientId);
        // 暂时多项相同的检查换成第一个
        List<CheckReports> values = new ArrayList<>(checkReports.stream()
                .collect(Collectors.toMap(CheckReports::getItemName, checkReport -> checkReport, (existing, replacement) -> existing))
                .values());
        RuleTreeVO ruleTreeVO = diagnoseRepository.getRuleTreeVOByDiseaseId(diagnoseEntity.getDiseaseId());
        //  光学相干断层成像（OCT）,扫描激光眼底检查(SLO)
        String input = ruleTreeVO.getInput();
        List<CheckReports> collect = values.stream().filter(e -> input.contains(e.getItemName())).collect(Collectors.toList());
        if (collect.size() != input.split(Constants.SPLIT).length)
            throw new AppException(400, "用户检查项目不够");
        Map<String, String> url = new HashMap<>();
        collect.forEach(e -> {
            List<ReportFiles> reportFiles = diagnoseRepository.getReportFile(e.getId());
            if (reportFiles == null || reportFiles.isEmpty()) throw new AppException(400, "用户检查项目不够");
            // todo 多个文件如何选择，暂时选择第一个
            ReportFiles reportFiles1 = reportFiles.get(0);
            String itemName = e.getItemName();
            url.put(itemName, reportFiles1.getFilePath());
        });
        DecisionTreeEngine decisionTreeEngine = treeFactory.openLogicTree(ruleTreeVO);
        return decisionTreeEngine.process(url);
    }
}
