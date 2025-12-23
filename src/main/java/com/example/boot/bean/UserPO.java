package com.example.boot.bean;


import lombok.Data;

/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/22 13:40
 */
@Data
public class UserPO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 数据库
     */
    private String database;
}
