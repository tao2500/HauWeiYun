package com.bdc.userService.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    //定义API接口映射路径
    public static final String DEFAULT_INCLUDE_PATTERN = "/user/.*";
    @Bean
    public Docket swaggerSpringfoxDocket() {
        //用于生成对应API接口文档的描述信息，可省略
        ApiInfo apiInfo = new ApiInfo("用户管理API接口测试文档","description",
                "termsOfServiceUrl","contact","version","","");
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .directModelSubstitute(java.time.LocalDate.class, String.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .select()
//                .paths(regex(DEFAULT_INCLUDE_PATTERN))//匹配路径生成对应接口文档
                .apis(RequestHandlerSelectors.basePackage("com.bdc.userService.controller"))
                .build();
        return docket;
    }
}
