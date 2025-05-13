package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.FollowupTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-12-21:47
 * @Description:
 * @Version: 1.0
 */
@Mapper
@Component
public interface IFollowupTemplateMapper extends BaseMapper<FollowupTemplate> {

}
