package com.example.boot.multdatasources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/23 15:07
 */
@Configuration
public class DataSourceConfiguration {

    @Autowired
    private DataSourceBuilder dataSourceBuilder;


    @Bean
    public DataSource dynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", dataSourceBuilder.getMasterDataSource());
        dataSourceMap.put("slave0", dataSourceBuilder.getSlave0DataSource());
        dataSourceMap.put("slave1", dataSourceBuilder.getSlave1DataSource());

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSourceMap.get("master"));
        // 设置所有数据源
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
