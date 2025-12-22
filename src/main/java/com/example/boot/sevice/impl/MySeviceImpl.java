package com.example.boot.sevice.impl;


import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.example.boot.mapper.MyMapper;
import com.example.boot.sevice.MySevice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2024/12/27 上午10:15
 */
@Service
public class MySeviceImpl implements MySevice {


    @Resource
    private MyMapper mapper;

    @Override
    @DSTransactional  // ds的事务注解
    public void helloAop(String name) {
        mapper.insert(1);
        mapper.insert(2);
    }
}
