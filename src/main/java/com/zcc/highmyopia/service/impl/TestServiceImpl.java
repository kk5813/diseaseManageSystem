package com.zcc.highmyopia.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.entity.Test;
import com.zcc.highmyopia.mapper.TestMapper;
import com.zcc.highmyopia.service.TestService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangyue
 * @since 2021-05-16
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {

}
