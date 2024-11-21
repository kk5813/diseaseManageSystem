package com.zcc.highmyopia.common.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 登录数据传输对象 (DTO)
 * 用于封装前端传递的用户登录信息，传输到后端进行验证
 */
@Data  // Lombok注解，自动生成getter、setter、toString等方法
//@ToString(exclude = "userPassword")  // 排除密码字段
public class LoginDto implements Serializable {

    @NotBlank(message = "登录名不能为空")  // 验证登录名不能为空，若为空则返回错误信息
    private String userLoginName;  // 用户登录名

    private String userPassword;  // 用户登录密码（建议加密处理）
}
