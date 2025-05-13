package com.zcc.highmyopia.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-13-15:23
 * @Description:
 * @Version: 1.0
 */
@Data
public class FollowupBatchDTO {
    List<String> followupIds;
    String visitDate;
}
