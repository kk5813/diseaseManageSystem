package com.zcc.highmyopia.config;

import com.zcc.highmyopia.shiro.AccountRealm;
import com.zcc.highmyopia.shiro.JwtFilter;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置类，主要用于配置安全管理器、会话管理、缓存管理以及过滤器
 */
@Configuration
public class ShiroConfig {

    @Autowired
    JwtFilter jwtFilter;  // 自定义 JWT 过滤器，用于替代传统的 Shiro Session 机制

    /**
     * 配置会话管理器，使用 Redis 来存储会话数据
     * @param redisSessionDAO Redis 会话数据访问对象，用于与 Redis 交互
     * @return SessionManager 会话管理器
     */
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 注入 Redis 会话管理 DAO，用于将会话数据存储在 Redis 中
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    /**
     * 配置 SecurityManager（安全管理器），用于管理认证、授权、会话和缓存
     * @param accountRealm 自定义 Realm，用于认证和授权逻辑
     * @param sessionManager 会话管理器，负责管理用户会话
     * @param redisCacheManager Redis 缓存管理器，负责缓存用户认证信息
     * @return SessionsSecurityManager 安全管理器
     */
    @Bean
    public SessionsSecurityManager securityManager(AccountRealm accountRealm,
                                                   SessionManager sessionManager,
                                                   RedisCacheManager redisCacheManager) {
        // 创建 DefaultWebSecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

        // 注入会话管理器
        securityManager.setSessionManager(sessionManager);

        // 注入 Redis 缓存管理器，指定主键字段为 userId
        redisCacheManager.setPrincipalIdFieldName("userId");
        securityManager.setCacheManager(redisCacheManager);

        return securityManager;
    }

    /**
     * 配置 Shiro 的过滤链，用于定义哪些请求需要认证，哪些请求不需要认证
     * @return ShiroFilterChainDefinition Shiro 的过滤链定义
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // 定义不需要认证的路径（例如登录页、注册页等），使用 anon 表示可以匿名访问
        // chainDefinition.addPathDefinition("/login", "anon");

        // 定义登出路径，Shiro 会自动处理
        chainDefinition.addPathDefinition("/logout", "logout");

        // 定义所有其他路径都需要通过 JWT 认证
        chainDefinition.addPathDefinition("/**", "jwt");
        return chainDefinition;
    }

    /**
     * 配置 ShiroFilterFactoryBean，将安全管理器和过滤链应用于 Shiro 的过滤器
     * @param securityManager 安全管理器
     * @param shiroFilterChainDefinition 过滤链定义
     * @return ShiroFilterFactoryBean Shiro 的过滤器工厂
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);  // 设置安全管理器

        // 配置自定义的过滤器，如 JWT 过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);  // 将 JWT 过滤器添加到过滤器链中
        shiroFilter.setFilters(filters);

        // 设置过滤链的定义，从 ShiroFilterChainDefinition 中获取
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }
}
