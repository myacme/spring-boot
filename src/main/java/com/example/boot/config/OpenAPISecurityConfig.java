package com.example.boot.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/11/14 15:22
 */

@Configuration
@EnableWebSecurity  // 启用 Spring Security 安全控制
@Profile("dev")     // 仅 dev 环境生效
// 声明 JWT 安全方案（当前项目未使用，属于冗余配置，可删除）
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPISecurityConfig {

    /**
     * 配置安全规则：控制路径访问权限
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 1. Swagger 相关路径必须认证（避免文档暴露）
                .antMatchers("/swagger-ui.html", "/v3/api-docs/**").authenticated()
                // 2. 其他业务接口允许匿名访问（可根据项目调整为 authenticated()）
                .anyRequest().permitAll()
                .and()
                // 3. 启用 HTTP Basic 认证（浏览器弹出账号密码登录框）
                .httpBasic()
                .and()
                // 4. 关闭 CSRF（开发环境简化操作，生产环境需评估）
                .csrf().disable();
        return http.build();
    }

    /**
     * 配置认证用户：内存存储（生产环境建议用数据库存储）
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // 密码编码器（自动适配 BCrypt 等加密方式）
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 定义用户：用户名=ljx，密码=123456
        UserDetails user = User.withUsername("ljx")
                .password(encoder.encode("123456"))
                // 角色（可用于细粒度权限控制）
                .roles("API_DOCS")
                .build();
        // 内存用户管理器（仅开发环境测试用）
        return new InMemoryUserDetailsManager(user);
    }
}
