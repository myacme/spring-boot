package com.example.boot.sevice.impl;


import com.example.boot.bean.UserPO;
import com.example.boot.mapper.MultipleDataSourcesMapper;
import com.example.boot.multdatasources.DataSourceContextHolder;
import com.example.boot.sevice.MultipleDataSourcesService;
import org.springframework.stereotype.Service;

/**
 *
 *
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/22 13:34
 */
@Service
public class MultipleDataSourcesServiceImpl implements MultipleDataSourcesService {

    private final MultipleDataSourcesMapper multipleDataSourcesMapper;

    public MultipleDataSourcesServiceImpl(MultipleDataSourcesMapper multipleDataSourcesMapper) {
        this.multipleDataSourcesMapper = multipleDataSourcesMapper;
    }

    @Override
    public UserPO getUser() {
        System.out.println(DataSourceContextHolder.getDataSourceKey());
        return multipleDataSourcesMapper.getUser();
    }
}
