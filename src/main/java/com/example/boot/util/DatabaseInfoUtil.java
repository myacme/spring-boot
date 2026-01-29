package com.example.boot.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 数据库信息工具类
 *
 * @author ljx
 * @version 1.0.0
 * @create 2026/1/27 16:37
 */
@Component
@Slf4j
public class DatabaseInfoUtil {

    private final DataSource dataSource;

    @Autowired
    public DatabaseInfoUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void printDatabaseInfo() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String productName = metaData.getDatabaseProductName();
            String productVersion = metaData.getDatabaseProductVersion();
            int majorVersion = metaData.getDatabaseMajorVersion();
            int minorVersion = metaData.getDatabaseMinorVersion();

            log.info("=== 数据库信息 ===");
            log.info("数据库产品名称: {}", productName);
            log.info("数据库产品版本: {} ({}.{}版)", productVersion, majorVersion, minorVersion);

            // 根据产品名称推断databaseId
            String databaseId = inferDatabaseId(productName);
            log.info("推断的DatabaseId: {}", databaseId);
            log.info("==================");
        } catch (SQLException e) {
            log.error("获取数据库信息失败", e);
        }
    }

    private String inferDatabaseId(String productName) {
        if (productName.toLowerCase().contains("mysql")) {
            return "mysql";
        } else if (productName.toLowerCase().contains("postgresql")) {
            return "postgres";
        } else if (productName.toLowerCase().contains("opengauss")) {
            return "opengauss";
        } else if (productName.toLowerCase().contains("oracle")) {
            return "oracle";
        } else if (productName.toLowerCase().contains("microsoft") || productName.toLowerCase().contains("sql server")) {
            return "sqlserver";
        } else {
            return "unknown";
        }
    }
}
