package com.zcc.highmyopia.AI.service;

import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public interface IDiagnoseService {

    DiagnoseResultEntity diagnose(DiagnoseEntity diagnoseEntity);

}