package com.example.boot.mapper;


import org.apache.ibatis.annotations.Mapper;

/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2026/1/28 09:39
 */
@Mapper
public interface DynamicSqlStatementMapper {

    String getDbType();
}
