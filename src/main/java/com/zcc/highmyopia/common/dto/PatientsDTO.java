package com.zcc.highmyopia.common.dto;

import lombok.Data;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Data
public class PatientsDTO {

    private String name;

    private Integer sex;

    private String birthdayBegin;

    private String birthdayEnd;

    private String visitTimeBegin;

    private String visitTimeEnd;

}
