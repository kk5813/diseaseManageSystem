package com.zcc.highmyopia.AIDiagnose.manager.modelManager;

import com.zcc.highmyopia.AIDiagnose.entity.request.ModelRequestVO;
import com.zcc.highmyopia.AIDiagnose.entity.response.DiagnoseResponse;

import java.util.List;

/**
 * @ClassName AIModelClient
 * @Description
 * @Author aigao
 * @Date 2026/1/18 21:44
 * @Version 1.0
 */
public interface AIModelClient {
    List<DiagnoseResponse> callModel(String url, ModelRequestVO modelRequest);
}
