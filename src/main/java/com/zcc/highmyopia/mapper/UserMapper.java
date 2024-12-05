package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
public interface UserMapper{

    @Select("select * from user")
    public List<User> list();
}
