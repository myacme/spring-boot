package com.example.boot.multdatasources;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源构建器 - 用于创建不同配置的数据源
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/23 16:08
 */
@Component
public class DataSourceBuilder {

    private final Environment env;

    // 使用缓存避免重复创建数据源
    private final ConcurrentHashMap<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    @Autowired
    public DataSourceBuilder(Environment env) {
        this.env = env;
    }

    public DataSource getMasterDataSource() {
        return getOrCreateDataSource("master");
    }

    public DataSource getSlave0DataSource() {
        return getOrCreateDataSource("slave0");
    }

    public DataSource getSlave1DataSource() {
        return getOrCreateDataSource("slave1");
    }

    /**
     * 通用方法：根据数据源名称创建或获取数据源
     *
     * @param dataSourceName 数据源名称
     * @return 配置好的数据源
     */
    private DataSource getOrCreateDataSource(String dataSourceName) {
        return dataSourceCache.computeIfAbsent(dataSourceName, this::createDataSource);
    }

    /**
     * 创建数据源
     *
     * @param dataSourceName 数据源名称
     * @return 配置好的数据源
     */
    private DataSource createDataSource(String dataSourceName) {
        String url = env.getProperty("spring.datasource.dynamic.datasource." + dataSourceName + ".url");
        String username = env.getProperty("spring.datasource.dynamic.datasource." + dataSourceName + ".username");
        String password = env.getProperty("spring.datasource.dynamic.datasource." + dataSourceName + ".password");
        String driverClassName = env.getProperty("spring.datasource.dynamic.datasource." + dataSourceName + ".driver-class-name");

        if (url == null || username == null || password == null || driverClassName == null) {
            throw new IllegalArgumentException("Missing required datasource configuration for: " + dataSourceName);
        }

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);

        // 设置Hikari连接池的常用参数
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(5);
        dataSource.setConnectionTimeout(30000);
        dataSource.setIdleTimeout(600000);
        dataSource.setMaxLifetime(1800000);
        dataSource.setLeakDetectionThreshold(60000);

        return dataSource;
    }
}
