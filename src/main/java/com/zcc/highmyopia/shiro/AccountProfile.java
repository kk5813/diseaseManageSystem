package com.zcc.highmyopia.shiro;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/*
 *
 *
 * */
@Data
public class AccountProfile implements Serializable {
    private Long userId;

    private String userLoginName;

    private String userName;

    private Integer userStatus;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    private String modifier;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
