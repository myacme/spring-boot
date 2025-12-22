package com.example.boot.util.添加配置;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author wuxiaoxiao
 * @version 1.0
 * @date 2023/11/28
 */
@Component
public class ConfigLoad implements CommandLineRunner {

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Autowired
    private RefreshConfigUtil refreshConfigUtil;

    @Override
    public void run(String... args) throws Exception {
        // 将我们自定义的作用域添加到Bean工厂中
        beanFactory.registerScope("refresh", BeanRefreshScope.getInstance());
        // 将获取到的数据添加到项目的Environment中
        refreshConfigUtil.refreshSftpPropertySource();
    }

}

