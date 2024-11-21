package com.zcc.highmyopia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题的配置类，允许前端发起跨域请求
 */
@Configuration  // 标识该类是一个配置类，Spring 会自动加载该配置
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 重写WebMvcConfigurer的addCorsMappings方法，配置跨域设置
     * @param registry CORS注册器，用于定义允许的跨域请求
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径的请求
                .allowedOriginPatterns("*")  // 允许所有的请求来源，支持通配符
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowCredentials(true)  // 允许携带Cookie等凭证
                .maxAge(3600)  // 缓存预检请求的时间，单位为秒，避免每次请求都进行预检
                .allowedHeaders("*");  // 允许的请求头，支持所有请求头
    }
}
