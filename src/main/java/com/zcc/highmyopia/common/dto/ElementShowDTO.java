package com.zcc.highmyopia.common.dto;

import com.zcc.highmyopia.hospital.entity.ElementEntity;
import com.zcc.highmyopia.hospital.entity.ElementVisionEntity;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import lombok.Data;

/**
 * @Author zcc
 * @Date 2024/12/28
 * @Description
 */
@Data
public class ElementShowDTO {

    private ElementEntity elementEntity;

    private VisitEntity visitEntity;

    private ElementVisionEntity elementVisionEntity;

}
