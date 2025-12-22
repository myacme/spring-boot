package com.example.boot.sevice.impl;


import com.example.boot.mapper.MyMapper;
import com.example.boot.sevice.TransactionSevice;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2024/12/27 上午10:15
 */
@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class MyTransactionImpl implements TransactionSevice {

/*
    @Transactional 默认捕获runtimeException

    声明式事务（@Transactional）：DataSourceTransactionManager  doBegin 获取dataSource 放入ThreadLocal里

    事务：
    1、A方法有@Transactional注解，B方法有，没有@Transactional注解，A调用B方法，都会事务成功
    2、A方法没有@Transactional注解，B方法有@Transactional注解，A调用B方法，事务失效
        解决方法：
            1、自调用  @Resource注入自己
            2、通过AopContext.currentProxy()获取代理对象，调用方法  类上加@EnableAspectJAutoProxy(exposeProxy=true)

    3、多线程事务：主线程有@Transactional注解
        主线程：ConnectionHolder connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        子线程：TransactionSynchronizationManager.bindResource(dataSource, connectionHolder);
                使用代理对象（自注入或者AopContext.currentProxy()）调用有声明式事务的方法，方法内的报错能回滚，方法为不会回滚
 */

    @Resource
    private MyMapper mapper;

    /**
     * 自调用  A方法没有@Transactional注解，B方法有@Transactional注解，A调用B方法，事务失效
     */
    @Resource
    MyTransactionImpl myTransactionImpl;

    @Resource
    DataSource dataSource;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testTransaction() throws InterruptedException {
        ConnectionHolder connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        mapper.insert(1);
//        helloAop1(name);
        //this调用事务失效 ； 自调用 事务生效
//        mySeviceTransactionImpl.helloAop1(name);
        //自调用 事务成功  获取当前类的动态代理对象
//        ((MyTransactionImpl) AopContext.currentProxy()).helloAop1(name);
        // 多线程事务
//        从 ThreadLocal<Map<Object, Object>> resources 中获取到 ConnectionHolder 对象，
//        MyTransactionImpl mySeviceTransaction = (MyTransactionImpl) AopContext.currentProxy();
        Thread thread = new Thread(() -> {
            // 将 ConnectionHolder 对象绑定到当前线程的 ThreadLocal 中
            TransactionSynchronizationManager.bindResource(dataSource, connectionHolder);
            myTransactionImpl.test1();
        });
        thread.start();
        thread.join();
    }

    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        mapper.insert1(2);
        int i = 1 / 0;
    }
}
