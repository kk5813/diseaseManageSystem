package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.ElementVision;

/**
 * @ClassName IPatientVisionRecordsService
 * @Description
 * @Author aigao
 * @Date 2024/12/24 11:14
 * @Version 1.0
 */
public interface IElementVisionService extends IService<ElementVision> {

    Result pageQuery(int pageNumber, int pageSize);

}
