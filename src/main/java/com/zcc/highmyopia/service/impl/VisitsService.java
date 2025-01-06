package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.disease.Disease;
import com.zcc.highmyopia.common.dto.CategoryCountDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.CategoryCountVO;
import com.zcc.highmyopia.common.vo.CategoryGroupCountVO;
import com.zcc.highmyopia.mapper.IDeptMapper;
import com.zcc.highmyopia.mapper.IVisitsMapper;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Visits;
import com.zcc.highmyopia.service.IDeptService;
import com.zcc.highmyopia.service.IVisitsService;
import lombok.RequiredArgsConstructor;
import oracle.sql.INTERVALDS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Service
@RequiredArgsConstructor
public class VisitsService extends ServiceImpl<IVisitsMapper, Visits> implements IVisitsService {

    @Resource
    private IVisitsMapper visitsMapper;
    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Override
    public List<CategoryGroupCountVO> categoryCount(CategoryCountDTO categoryCountDTO) {
        // 数据查到的列表
        List<Visits> visitsList = visitsMapper.categoryCount(categoryCountDTO);

        // 提取所有的诊断名称
        List<String> diagNameList = visitsList.stream()
                .map(Visits::getDiagName)
                .collect(Collectors.toList());

        List<CategoryGroupCountVO> categoryGroupCountVOList = new ArrayList<>();

        Map<String, Map<String, Integer>> map = new HashMap<>();
        for (String diagName : diagNameList) {
            Disease disease = Disease.findByName(diagName);
            if (disease != null) {
                Map<String, Integer> categoryMap = map.getOrDefault(disease.getGroup().getName(), new HashMap<>());
                String categoryName = disease.getName();
                categoryMap.put(categoryName, categoryMap.getOrDefault(categoryName, 0) + 1);
                map.put(disease.getGroup().getName(), categoryMap);
            }
        }

        for (Map.Entry<String, Map<String, Integer>> groupEntry : map.entrySet()) {
            String groupName = groupEntry.getKey();
            Map<String, Integer> categoryMap = groupEntry.getValue();

            int totalCount = categoryMap.values().stream().mapToInt(Integer::intValue).sum();

            List<CategoryCountVO> categoryCounts = new ArrayList<>();

            for (Map.Entry<String, Integer> categoryEntry : categoryMap.entrySet()) {
                String categoryName = categoryEntry.getKey(); // 分类名称
                int count = categoryEntry.getValue(); // 分类的计数

                CategoryCountVO categoryCountVO = new CategoryCountVO(categoryName, count);
                categoryCounts.add(categoryCountVO);
            }

            CategoryGroupCountVO categoryGroupCountVO = new CategoryGroupCountVO(groupName, totalCount, categoryCounts);
            categoryGroupCountVOList.add(categoryGroupCountVO);
        }
        return categoryGroupCountVOList;
    }

    @Override
    public IPage<Visits> getVisitsPage(int page, int size, String diagName,String startTime, String endTime, Long patientID) {
        LambdaQueryWrapper<Visits> visitsLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<Visits> visitsPage = new Page<>(page, size);
        if (StringUtils.isNotBlank(diagName)) {
            visitsLambdaQueryWrapper.like(Visits::getDiagName, diagName);
        }
        if (StringUtils.isNotBlank(startTime)) {
            visitsLambdaQueryWrapper.ge(Visits::getDiagTime, startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            visitsLambdaQueryWrapper.le(Visits::getDiagTime, endTime);
        }
        if (patientID != null && StringUtils.isNotBlank(patientID.toString())) {
            visitsLambdaQueryWrapper.eq(Visits::getPatientId, patientID);
        }
        IPage<Visits> visitsIPage = visitsMapper.selectPage(visitsPage,visitsLambdaQueryWrapper);
        return visitsIPage;
    }

}
