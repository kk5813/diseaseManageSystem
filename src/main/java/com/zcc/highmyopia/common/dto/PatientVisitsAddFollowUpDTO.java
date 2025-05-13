package com.zcc.highmyopia.common.dto;

import lombok.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-13-11:13
 * @Description:
 * @Version: 1.0
 */
@Data
public class PatientVisitsAddFollowUpDTO {
    private String templateId; // 随访模板Id

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Info{
        private String patientId;  // 病人ID，唯一标识患者

        private String deptId; // 科室ID

        private String doctorId; // 医生ID

        private String visitNumber; // 就诊号
    }
    private List<Info> infos;

    // 对 templateId 和 infos 的部分内容进行哈希计算 生成摘要，用于判断是否在重复请求。
    public String generateHash()  {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.update(templateId.getBytes(StandardCharsets.UTF_8));
        if (infos != null && !infos.isEmpty()) {
            int limit = Math.min(3, infos.size());
            for (int i = 0; i < limit; i++) {
                digest.update(infos.get(i).toString().getBytes(StandardCharsets.UTF_8));
            }
            digest.update(infos.get(infos.size() - 1).toString().getBytes(StandardCharsets.UTF_8));
        }
        byte[] hashBytes = digest.digest();
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
