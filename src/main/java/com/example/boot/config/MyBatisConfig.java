package com.example.boot.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * MyBatis 配置 不同数据库执行不同的sql
 *
 * @author ljx
 * @version 1.0.0
 * @create 2026/1/28 09:36
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        // 为不同的数据库厂商设置标识符
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("PostgreSQL", "postgres");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}