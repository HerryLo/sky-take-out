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
                .description("苍穹外卖项目接口文档"));

    }
}