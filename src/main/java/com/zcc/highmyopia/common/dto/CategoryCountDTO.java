package com.zcc.highmyopia.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.sf.saxon.regex.charclass.Categories;

import java.time.LocalDateTime;

/**
 * @Author zcc
 * @Date 2024/12/22
 * @Description
 */
@Data
public class CategoryCountDTO {

    private String beginTime;

    private String endTime;
}
