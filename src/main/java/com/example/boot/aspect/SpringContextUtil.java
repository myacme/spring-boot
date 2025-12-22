package com.example.boot.aspect;

import com.example.boot.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author MyAcme
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    // 第二种
    @Autowired
    private ApplicationContext applicationContext;

    public void someMethod() {
        User repo = applicationContext.getBean(User.class);
    }
}
