package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.common.vo.FollowupTemplateVO;
import com.zcc.highmyopia.po.FollowupTemplate;
import org.springframework.web.bind.annotation.RequestParam;

public interface IFollowupTemplateService extends IService<FollowupTemplate> {
    public IPage<FollowupTemplateVO> searchFollowupTemplate(String dateStart, String dateEnd,int pageNo, int pageSize, String deptName, String templateName,
                                                             String modifyName, int intervalValue, String visitPlan);
}
