package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangyue
 * @since 2021-02-01
 */
@Mapper
@Component
public interface IUserMapper extends BaseMapper<User> {

    @Select("select * from user")
    List<User> list();

    List<User> SearchUser(User user);
}
