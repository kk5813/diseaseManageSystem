package com.zcc.highmyopia.service;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangyue
 * @since 2021-02-01
 */
public interface IUserService extends IService<User> {

    Result getUsersPage(int page, int size);
}
