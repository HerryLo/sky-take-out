package com.liuheng.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .openapi("3.0.1")  // ✅ 显式指定版本，关键！
                .schemaRequirement("tokenAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)  // 改为 APIKEY
                    .in(SecurityScheme.In.HEADER)
                    .name("token"))  // 自定义名称生效！
                .info(new Info()
                .title("苍穹外卖 API 文档")
                .version("1.0.0")
                .description("苍穹外卖项目接口文档"));

    }
}