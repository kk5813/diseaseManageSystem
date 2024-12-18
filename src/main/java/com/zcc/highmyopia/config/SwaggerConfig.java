package com.zcc.highmyopia.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description http://localhost:8081/swagger-ui.html
 */
@Configuration  //配置类
@EnableSwagger2 // 开启Swagger2的自动配置
public class SwaggerConfig {

    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                // RequestHandlerselectors.配罩要扫播接口的方式
                // basePackage("com.example.demo3"):指定要扫描的包
                // any ():扫描全部
                //none():个扫描
                //withclassAnnotation，扫描类上的注解，参数足一个注解的反射对象 withClassAnnotation(RestController.class) 扫描类上有RestController注解的类
                // withMethodAnnotation:扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.example.demo3"))
                //过滤路径 ant("/code/**")只扫描带有code的路径的注解
                //.paths(PathSelectors.ant("/code/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        //作者信息
        Contact contact = new Contact("zcc", "", "2287996531@qq.com");
        return new ApiInfo("朱畅畅的swaggerApi文档",
                "患者管理系统",
                "1.0版本号",
                "地址",
                contact, "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }

}
