package com.zcc.highmyopia.common.vo;

import com.zcc.highmyopia.po.Element;
import com.zcc.highmyopia.po.User;
import lombok.Data;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Data
public class UserVO {

    private Long total;

    private List<User> users;

}
