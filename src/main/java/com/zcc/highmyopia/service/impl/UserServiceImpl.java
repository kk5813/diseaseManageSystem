package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.entity.User;
import com.zcc.highmyopia.mapper.UserMapper;
import com.zcc.highmyopia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangyue
 * @since 2021-02-01
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    public Result getUsersPage(int page, int size) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<User> userPage = new Page<>(page, size);
        IPage<User> userIPage = userMapper.selectPage(userPage,userLambdaQueryWrapper);
        return Result.succ(userIPage);
    }
}
