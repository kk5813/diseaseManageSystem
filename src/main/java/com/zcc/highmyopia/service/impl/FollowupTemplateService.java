package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.vo.FollowupTemplateVO;
import com.zcc.highmyopia.mapper.IDeptMapper;
import com.zcc.highmyopia.mapper.IFollowupTemplateMapper;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.FollowupTemplate;
import com.zcc.highmyopia.service.IDeptService;
import com.zcc.highmyopia.service.IFollowupTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-12-21:54
 * @Description:
 * @Version: 1.0
 */
@Service
public class FollowupTemplateService extends ServiceImpl<IFollowupTemplateMapper, FollowupTemplate> implements IFollowupTemplateService {
    @Resource
    private IDeptMapper deptMapper;

    @Resource
    private IDeptService deptService;

    @Resource
    private IFollowupTemplateMapper followupTemplateMapper;

    @Override
    public IPage<FollowupTemplateVO> searchFollowupTemplate(String dateStart, String dateEnd, int pageNo, int pageSize, String deptName, String templateName, String modifier, int intervalValue, String visitPlan) {
        LambdaQueryWrapper<FollowupTemplate> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.ge(StringUtils.isNotBlank(dateStart), FollowupTemplate::getUpdateTime, dateStart);
        lambdaQueryWrapper.le(StringUtils.isNotBlank(dateEnd), FollowupTemplate::getUpdateTime, dateEnd);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(templateName), FollowupTemplate::getTemplateName, templateName);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(modifier), FollowupTemplate::getModifier, modifier);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(visitPlan), FollowupTemplate::getVisitPlan, visitPlan);
        lambdaQueryWrapper.eq(intervalValue > 0, FollowupTemplate::getIntervalValue, intervalValue);
        // 排除被删掉的模板
        lambdaQueryWrapper.ne(FollowupTemplate::getIsActive,0);
        if(StringUtils.isNotBlank(deptName)){
            LambdaQueryWrapper<Dept> deptLambdaQueryWrapper = Wrappers.lambdaQuery();
            deptLambdaQueryWrapper.like(Dept::getDeptName, deptName).select(Dept::getId);
            List<Long> deptIds = deptMapper.selectList(deptLambdaQueryWrapper).stream().map(Dept::getId).distinct().collect(Collectors.toList());
            lambdaQueryWrapper.in(FollowupTemplate::getDeptId, deptIds);
        }
        IPage<FollowupTemplate> followupTemplateIPage = followupTemplateMapper.selectPage(new Page<>(pageNo,pageSize), lambdaQueryWrapper);
        IPage<FollowupTemplateVO> followupTemplateVOIPage = followupTemplateIPage.convert(followupTemplate -> {
            FollowupTemplateVO followupTemplateVO = FollowupTemplateVO.FollowupTemplateToVo(followupTemplate);
            followupTemplateVO.setDeptName((deptService.getDeptById(
                    Long.valueOf(followupTemplate.getDeptId()))
                    .getDeptName()));
            return followupTemplateVO;
        });

        return followupTemplateVOIPage;
    }

}
