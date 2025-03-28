package com.zcc.highmyopia.AI.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sun.media.sound.MidiOutDeviceProvider;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeLineVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.*;
import com.zcc.highmyopia.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Slf4j
@Repository
public class DiagnoseRepository implements IDiagnoseRepository {

    @Resource
    private IRedisService redisService;

    @Resource
    private IModelNodeService modelNodeService;

    @Resource
    private ICheckReportsService checkReportsService;

    @Resource
    private IModelDiseaseService modelDiseaseService;

    @Resource
    private IReportFilesService reportFilesService;

    @Resource
    private IModelLineService modelLineService;

    @Resource
    private IReportFilesMapper reportFilesMapper;

//    @Override
//    public List<ModelNode> getModel() {
//        String cacheKey = Constants.RedisKey.MODEL;
//        List<ModelNode> modelNodes = redisService.getValue(cacheKey);
//        if (modelNodes != null) return modelNodes;
//
//        List<ModelNode> list = modelService.list();
//        if (list == null || list.isEmpty())
//            throw new AppException(400, "未配置模型服务");
//        redisService.setValue(cacheKey, list);
//        return list;
//    }

    @Override
    public List<CheckReports> getCheckReport(Long patientId) {
        List<CheckReports> list = checkReportsService.list(new LambdaQueryWrapper<CheckReports>()
                .eq(CheckReports::getPatientId, patientId)
                .ge(CheckReports::getCheckTime, LocalDate.now().atStartOfDay()) // 当天开始时间
                .lt(CheckReports::getCheckTime, LocalDate.now().plusDays(1).atStartOfDay())); // 下一天的开始时间
        if (list == null) return new ArrayList<>();

        // 返回最新的记录列表
        return new ArrayList<>(list.stream()
                .collect(Collectors.toMap(
                        CheckReports::getItemName, e -> e,
                        (existing, replacement) -> existing.getCheckTime().isAfter(replacement.getCheckTime()) ? existing : replacement // 比较 checkTime，保留最新的
                ))
                .values());
    }

    @Override
    public List<CheckReports> getCheckReportByVisitNumber(String visitNumber) {
        return checkReportsService.getCheckReportByVisitNumber(visitNumber);
    }


    @Override
    public List<ReportFiles> getReportFile(Long id) {
        return reportFilesMapper.getReportFile(id);
    }

    @Override
    public RuleTreeVO getRuleTreeVOByDiseaseId(Integer diseaseId) {
        // 优先从缓存中获取
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + diseaseId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (null != ruleTreeVOCache) return ruleTreeVOCache;

        ModelDisease disease = modelDiseaseService.getModelDiseaseByDiseaseId(diseaseId);
        List<ModelNode> modelNodes = modelNodeService.getModelNodeByDiseaseId(diseaseId);
        List<ModelLine> modelLines = modelLineService.getModelLineByDiseaseId(diseaseId);
        // treeNodeLine转为map结构   起点 -> treeNodeLine
        Map<Integer, List<RuleTreeNodeLineVO>> treeNodeLineMap = new HashMap<>();
        for (ModelLine modelLine : modelLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .id(modelLine.getId())
                    .modelFrom(modelLine.getModelFrom())
                    .modelTo(modelLine.getModelTo())
                    .limitValue(modelLine.getLimitValue())
                    .useOutputUrl(modelLine.getUseOutputUrl())
                    .build();
            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = treeNodeLineMap.computeIfAbsent(modelLine.getModelFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
        }
        // treeNode转换为Map结构
        Map<Integer, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
        for (ModelNode modelNode : modelNodes) {
            RuleTreeNodeVO treeNodeVO = RuleTreeNodeVO.builder()
                    .id(modelNode.getId())
                    .api(modelNode.getApi())
                    .name(modelNode.getName())
                    .input(modelNode.getInput())
                    .treeNodeLineVOList(treeNodeLineMap.get(modelNode.getId()))
                    .build();
            treeNodeMap.put(modelNode.getId(), treeNodeVO);
        }

        // 3. 构建 Rule Tree
        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .id(disease.getId())
                .input(disease.getInput())
                .startNode(disease.getStartNode())
                .desc(disease.getDescription())
                .name(disease.getName())
                .treeNodeMap(treeNodeMap)
                .build();
        redisService.setValue(cacheKey, ruleTreeVODB);
        return ruleTreeVODB;
    }

    @Override
    public int getDownLoadReportFileCountByVisitNumber(String visitNumber) {
        return reportFilesMapper.getDownLoadReportFileCountByVisitNumber(visitNumber);
    }

}
