package com.zcc.highmyopia.common.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author zcc
 * @Date 2025/12/17
 * @Description
 */
@Data
public class ModelDiseaseDTO {

    private String name;

    private String description;

    private String input;

    private Integer startNode;

}
