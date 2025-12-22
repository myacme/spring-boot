package com.example.boot.config;


import com.example.boot.Interceptor.TokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/21 10:41
 */

//@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	private static final Logger log = LoggerFactory.getLogger(WebConfiguration.class);

	@Resource
	private TokenInterceptor tokenInterceptor;

	/**
	 * 跨域处理
	 *
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedHeaders("*")
				.allowedMethods("*")
				.allowedOriginPatterns("*")
				.allowCredentials(true);
	}

	/**
	 * 异步请求配置
	 *
	 * @param configurer
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
		configurer.setDefaultTimeout(30000);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> excludePath = new ArrayList<>();
		//排除拦截，除了注册登录(此时还没token)，其他都拦截
		//登录
		excludePath.add("/**/accountLogin");
		//手机登录
		excludePath.add("/**/telLogin");
		//验证码
		excludePath.add("/**/numberCode");
		//排除swagger地址
		excludePath.add("/**/swagger-ui/index.html");
		excludePath.add("/**/swagger-resources/**");
		excludePath.add("/**/v3/api-docs/**");
		registry.addInterceptor(tokenInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(excludePath);
		WebMvcConfigurer.super.addInterceptors(registry);
		log.info("---注册JWT拦截器---");
	}
}
