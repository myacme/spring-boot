package com.example.boot.multdatasources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/23 15:38
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DataSourceInterceptor dataSourceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dataSourceInterceptor)
                .addPathPatterns("/**"); // 根据需要设置拦截路径
    }
}
