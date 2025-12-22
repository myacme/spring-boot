package com.example.boot.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.boot.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2024/12/27 上午10:18
 */
@Mapper
public interface MyMapper {

    int insert(int v);
    int insert1(int v);

    /**
     * 动态数据源  根据 dsName  动态连接数据库
     * @param dsName
     * @param tableName
     * @return
     */
    @DS("#dsName") // 使用SpEL表达式动态指定数据源
    @Select("SELECT * FROM ${tableName}")
    List<User> selectByDynamicDS(@Param("dsName") String dsName, @Param("tableName") String tableName);

}
