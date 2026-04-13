package com.liuheng.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableKnife4j
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .openapi("3.0.1")  // ✅ 显式指定版本，关键！
                .info(new Info()
                .title("苍穹外卖 API 文档")
                .version("1.0.0")
                .description("苍穹外卖项目接口文档"))
        ;

    }

    /**
     * 管理端接口分组
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理端接口")      // 分组名称，数字前缀可控制排序
                .packagesToScan("com.liuheng.controller.admin")   // 匹配路径规则
                .build();
    }

    /**
     * 用户端接口分组
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户端接口")
                .packagesToScan("com.liuheng.controller.user")
                .build();
    }
}