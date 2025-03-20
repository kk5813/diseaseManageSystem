package com.zcc.highmyopia.AI.service;

import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public interface IDiagnoseService {

    List<List<DiagnoseResultEntity>> diagnose(DiagnoseEntity diagnoseEntity, String isNeedDownloadAgain) throws Exception;

}
