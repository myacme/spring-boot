package com.example.boot.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.boot.bean.User;
import com.example.boot.bean.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/22 13:37
 */
@Mapper
public interface MultipleDataSourcesMapper {

    /**
     * 动态数据源  根据 dsName  动态连接数据库
     * @param dsName ds
     * @param tableName   table
     * @return List<User>
     */
    @DS("#dsName") // 使用SpEL表达式动态指定数据源
//    @Select("SELECT * FROM ${tableName}")
    List<User> selectByDynamicDS(@Param("dsName") String dsName, @Param("tableName") String tableName);


    UserPO getUser();






}
