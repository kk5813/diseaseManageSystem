package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Site;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
public interface ISiteService extends IService<Site> {
    Site getSiteById(Long siteId);
}
