package com.example.boot.controller;


import com.example.boot.sevice.DynamicSqlStatementService;
import com.example.boot.util.DatabaseInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 动态SQL测试
 *
 * @author ljx
 * @version 1.0.0
 * @create 2026/1/28 09:37
 */
@RestController
public class DynamicSqlStatementController {

    private final DynamicSqlStatementService dynamicSqlStatementService;

    @Autowired
    DatabaseInfoUtil databaseInfoUtil;
    @Autowired
    public DynamicSqlStatementController(DynamicSqlStatementService dynamicSqlStatementService) {
        this.dynamicSqlStatementService = dynamicSqlStatementService;
    }


    @GetMapping("/get-db-type")
    public String getDbType() {
        databaseInfoUtil.printDatabaseInfo();
        return dynamicSqlStatementService.getDbType();
    }
}
