package com.zcc.highmyopia.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @ClassName UserDto
 * @Description
 * @Author aigao
 * @Date 2024/12/23 21:25
 * @Version 1.0
 */
@Data
public class UserDto {

    private String userLoginName;

    private String userName;

    private String userPassword;

    private Integer userStatus;

    private String modifier;
}
