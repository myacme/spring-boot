package com.example.boot.util.添加配置;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuxiaoxiao
 * @version 1.0
 * @date 2023/11/28
 */
public class BeanRefreshScope implements Scope {

    public static final String SCOPE_REFRESH = "refresh";

    private static final BeanRefreshScope INSTANCE = new BeanRefreshScope();

    // 用此map来缓存bean
    private ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();

    // 禁止实例化
    private BeanRefreshScope() {
    }

    public static BeanRefreshScope getInstance() {
        return INSTANCE;
    }

    // 清理当前实例缓存的map
    public static void clean() {
        INSTANCE.beanMap.clear();
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {

        Object bean = beanMap.get(name);
        if (bean == null) {
            bean = objectFactory.getObject();
            beanMap.put(name, bean);
        }

        return bean;
    }

    @Override
    public Object remove(String name) {
        return beanMap.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

}

