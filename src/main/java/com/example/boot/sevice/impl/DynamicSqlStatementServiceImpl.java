package com.example.boot.sevice.impl;


import com.example.boot.mapper.DynamicSqlStatementMapper;
import com.example.boot.sevice.DynamicSqlStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2026/1/28 09:39
 */
@Service
public class DynamicSqlStatementServiceImpl implements DynamicSqlStatementService {

    private final DynamicSqlStatementMapper dynamicSqlStatementMapper;

    @Autowired
    public DynamicSqlStatementServiceImpl(DynamicSqlStatementMapper dynamicSqlStatementMapper) {
        this.dynamicSqlStatementMapper = dynamicSqlStatementMapper;
    }

    @Override
    public String getDbType() {
        return dynamicSqlStatementMapper.getDbType();
    }
}
