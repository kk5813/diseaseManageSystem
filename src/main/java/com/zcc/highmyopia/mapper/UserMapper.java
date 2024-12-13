package com.zcc.highmyopia.mapper;

import com.zcc.highmyopia.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user")
    public List<User> list();

    Page<User> findAll(Pageable pageable);
}
