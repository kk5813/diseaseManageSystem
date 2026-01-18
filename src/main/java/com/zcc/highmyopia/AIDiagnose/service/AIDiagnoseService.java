package com.zcc.highmyopia.AIDiagnose.service;

import com.zcc.highmyopia.AIDiagnose.entity.request.DiagnoseRequestDTO;
import com.zcc.highmyopia.AIDiagnose.entity.response.DiagnoseResponse;

import java.util.List;

/**
 * @ClassName AIDiagnoseService
 * @Description
 * @Author aigao
 * @Date 2026/1/17 22:01
 * @Version 1.0
 */
public interface AIDiagnoseService {
    List<DiagnoseResponse> diagnose(DiagnoseRequestDTO request) throws Exception;
}
