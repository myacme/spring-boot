package com.example.boot.util.添加配置;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuxiaoxiao
 * @version 1.0
 * @date 2023/11/28
 */
@Component
public class RefreshConfigUtil {
    // 获取环境配置对象
    @Autowired
    private ConfigurableEnvironment environment;


    /**
     * 模拟改变数据库中的配置信息
     */
    public void updateDbConfigInfo() {
        // 更新context中的PropertySource配置信息
        this.refreshSftpPropertySource();
        // 清空BeanRefreshScope中所有bean的缓存
        BeanRefreshScope.getInstance();
        BeanRefreshScope.clean();
    }

    public void refreshSftpPropertySource() {
        /*
          @Value中的数据源来源于Spring的「org.springframework.core.env.PropertySource」中
         * 此处为获取项目中的全部@Value相关的数据
         */
        MutablePropertySources propertySources = environment.getPropertySources();
        // 模拟从数据库中获取配置信息
        Map<String, Object> sftpInfoFromDb = new HashMap<>(2);
        // 将数据库查询到的配置信息放到MapPropertySource中(MapPropertySource类是spring提供的一个类,是PropertySource的子类)
        MapPropertySource sftpPropertySource = new MapPropertySource("key", sftpInfoFromDb);
        // 将配置信息放入 环境配置对象中
        propertySources.addLast(sftpPropertySource);
    }
}

