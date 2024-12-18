package com.zcc.highmyopia.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    //private static final String AUTH_HEADER_NAME = "token";

    //Knife4j扩展对象
    //@Autowired
    //private OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                //加了ApiOperation注解的方法，才生成接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //特定包下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.zcc.highmyopia.controller"))
                .paths(PathSelectors.ant("/api/**"))
                //.paths(PathSelectors.any())
                .build();
               // .extensions(openApiExtensionResolver.buildExtensions("default"));
                //设置全局token
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts());
                //每个接口传token
                //.globalRequestParameters(globalRequestParameters());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("爱尔眼科医院系统Api")
                .description("爱尔眼科医院系统接口文档")
                .termsOfServiceUrl("https://localhost:8081")
                .contact(new Contact("朱畅畅", "https://xxx", "2287996531@qq.com"))
                .version("1.0版本号")
                .build();
    }

//    private List<SecurityScheme> securitySchemes() {
//        return Arrays.asList(new ApiKey(AUTH_HEADER_NAME, "auth", In.HEADER.name()));
//    }

//    private List<SecurityContext> securityContexts() {
//        List<SecurityContext> securityContexts = new ArrayList<>();
//        securityContexts.add(SecurityContext
//                .builder()
//                .securityReferences(securityReferences())
//                .operationSelector(operationContext -> operationContext.requestMappingPattern().startsWith("/api/"))
//                .build());
//        return securityContexts;
//    }
//
//    private List<SecurityReference> securityReferences() {
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[] {new AuthorizationScope("global", "accessEverything")};
//        List<SecurityReference> securityReferences = new ArrayList<>();
//        securityReferences.add(new SecurityReference(AUTH_HEADER_NAME, authorizationScopes));
//        return securityReferences;
//    }
//
//    private List<RequestParameter> globalRequestParameters() {
//        return Arrays.asList(new RequestParameterBuilder()
//                .name(AUTH_HEADER_NAME)
//                .description("access token")
//                .in(ParameterType.HEADER)
//                .required(false)
//                .build());
//    }

}
//package com.zcc.highmyopia.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//
///**
// * @Author zcc
// * @Date 2024/12/17
// * @Description http://localhost:8081/swagger-ui.html
// */
//@Configuration  //配置类
//@EnableSwagger2 // 开启Swagger2的自动配置
//public class SwaggerConfig {
//
//    public Docket docket() {
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
//                .select()
//                // RequestHandlerselectors.配罩要扫播接口的方式
//                // basePackage("com.example.demo3"):指定要扫描的包
//                // any ():扫描全部
//                //none():个扫描
//                //withclassAnnotation，扫描类上的注解，参数足一个注解的反射对象 withClassAnnotation(RestController.class) 扫描类上有RestController注解的类
//                // withMethodAnnotation:扫描方法上的注解
//                .apis(RequestHandlerSelectors.basePackage("com.zcc.highmyopia.controller"))
//                //过滤路径 ant("/code/**")只扫描带有code的路径的注解
//                //.paths(PathSelectors.ant("/code/**"))
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        //作者信息
//        Contact contact = new Contact("zcc", "", "2287996531@qq.com");
//        return new ApiInfo("朱畅畅的swaggerApi文档",
//                "患者管理系统",
//                "1.0版本号",
//                "地址",
//                contact, "Apache 2.0",
//                "http://www.apache.org/licenses/LICENSE-2.0",
//                new ArrayList());
//    }
//
//}
