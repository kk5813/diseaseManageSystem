package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.mapper.IUserMapper;
import com.zcc.highmyopia.service.IRedisService;
import com.zcc.highmyopia.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

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
public class UserService extends ServiceImpl<IUserMapper, User> implements IUserService {

    private final IUserMapper userMapper;

    private final IRedisService redisService;

    @Override
    public Result getUsersPage(int page, int size) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        Page<User> userPage = new Page<>(page, size);
        IPage<User> userIPage = userMapper.selectPage(userPage,userLambdaQueryWrapper);
        return Result.succ(userIPage);
    }

    @Override
    public List<User> SearchUser(User user) {
        return userMapper.SearchUser(user);
    }

    @Override
    public String getUserBridgeRedis(String userId) {
        String cacheKey = Constants.RedisKey.USER + userId;
        String userName = redisService.getValue(cacheKey);
        if(StringUtils.isNotBlank(userName)){
            return userName;
        }
        User user = userMapper.selectOne(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId));
        redisService.setValue(cacheKey,user.getUserName(), 1000 * 60 * 10); // 10min
        return user.getUserName();
    }
}
