package com.example.boot.multdatasources;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("sharding_db_0", createDataSource("sharding_db_0"));
        dataSourceMap.put("sharding_db_1", createDataSource("sharding_db_1"));

        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(createDataSource("sharding_db_0"));

        return dynamicDataSource;
    }

    private DataSource createDataSource(String dbName) {
        // 创建具体数据源实例
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/" + dbName);
        dataSource.setUsername("root");
        dataSource.setPassword("mysql@1qaz");
        return dataSource;
    }
}
