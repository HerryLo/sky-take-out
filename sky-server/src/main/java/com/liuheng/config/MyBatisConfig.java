package com.liuheng.config;

import com.liuheng.interceptor.SqlErrorInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public SqlErrorInterceptor sqlErrorInterceptor() {
        return new SqlErrorInterceptor();
    }
}
