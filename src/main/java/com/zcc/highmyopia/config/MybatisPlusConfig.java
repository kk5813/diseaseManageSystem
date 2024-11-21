package com.zcc.highmyopia.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * MyBatis-Plus 的配置类
 * 主要配置了分页插件和事务管理
 */
@Configuration  // 声明这是一个配置类，Spring 会自动将其作为配置加载
@EnableTransactionManagement  // 开启 Spring 的事务管理功能，使 @Transactional 注解生效
@MapperScan("com.zcc.highmyopia.mapper")  // 扫描指定包下的 Mapper 接口，MyBatis 将自动生成这些接口的实现
public class MybatisPlusConfig {

    /**
     * 配置 MyBatis-Plus 的分页插件
     * 分页拦截器会拦截查询请求，自动处理分页逻辑
     * @return PaginationInterceptor 分页插件实例
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(500);  // 设置单页最大记录数，避免一次查询过多数据
        return paginationInterceptor;

    }
}
