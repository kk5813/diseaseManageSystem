package com.zcc.highmyopia.AIDiagnose.manager.dbManager;

import java.util.List;

/**
 * @ClassName modelConfigDB
 * @Description
 * @Author aigao
 * @Date 2026/1/17 23:15
 * @Version 1.0
 */
public interface ModelConfigDB {
    List<String> getNeedCheckItem(Integer diseaseId);

    String getUrlByDiseaseId(Integer diseaseId);

    void evictCache(Integer diseaseId);
}
