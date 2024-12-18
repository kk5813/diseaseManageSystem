package com.zcc.highmyopia.service.old.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.po.old.Test;
import com.zcc.highmyopia.mapper.old.TestMapper;
import com.zcc.highmyopia.service.old.TestService;
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
