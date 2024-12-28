package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.common.dto.CategoryCountDTO;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.vo.CategoryGroupCountVO;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Visits;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
public interface IVisitsService extends IService<Visits> {
    List<CategoryGroupCountVO> categoryCount(CategoryCountDTO categoryCountDTO);

    IPage<Visits> getVisitsPage(int page, int size, String diagName);
}
