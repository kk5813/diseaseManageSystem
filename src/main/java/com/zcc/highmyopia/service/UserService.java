package com.zcc.highmyopia.service;

import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liangyue
 * @since 2021-02-01
 */
public interface UserService extends IService<User> {

    Result getUsersPage(int page, int size);
}
