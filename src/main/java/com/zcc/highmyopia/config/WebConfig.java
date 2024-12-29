package com.zcc.highmyopia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题的配置类，允许前端发起跨域请求
 */
@Configuration  // 标识该类是一个配置类，Spring 会自动加载该配置
public class WebConfig implements WebMvcConfigurer {

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

    @Value("${hospital.filePath}")
    private String targetPath;

    @Value("${hospital.pdf2ImgPath}")
    private String PDFToImgRelativePath;

    @Value("${hospital.localImage}")
    private String ImagePathLocalHost;

    // 1. 添加多个静态资源目录映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(ImagePathLocalHost+"/**")
                //addResourceHandler("/images/**")
                .addResourceLocations("file:/"+targetPath+"/")
                //.addResourceLocations("file:/E:/Download/project/")
                .setCachePeriod(3600);
        // 设置缓存时间为3600秒

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/public/**")
                .addResourceLocations("classpath:/public/")
                .setCachePeriod(3600);
    }
}
