package com.example.boot.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/11/14 15:18
 */
@Configuration
// 仅在 dev 环境生效（生产环境建议关闭 Swagger）
@Profile("dev")
public class OpenApiConfig {

    /**
     * 配置 OpenAPI 文档元信息 + 安全方案
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                // 1. 文档基础信息（显示在 Swagger UI 顶部）
                .info(new Info()
                        // 文档标题
                        .title("接口文档")
                        // 文档版本
                        .version("1.0")
                        // 文档描述（可写接口规范、注意事项）
                        .description("项目API文档说明")
                        // 联系人（维护者信息）
                        .contact(new Contact().name("ljx")))
                // 2. 全局安全需求：声明所有接口需通过 "basicAuth" 认证（可按需关闭）
//                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                // 3. 定义安全方案：此处为 HTTP Basic 认证（账号密码登录）
                .components(new Components()
                        // 安全方案名称（需与上方 SecurityRequirement 对应）
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        // 认证类型：HTTP
                                        .type(SecurityScheme.Type.HTTP)
                                        // 认证方式：basic（基础认证）
                                        .scheme("basic")));
    }
}