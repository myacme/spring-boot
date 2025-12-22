package com.example.boot.fliter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * 〈〉
 *
 * @author ljx
 * @version 1.0.0
 * @create 2021/11/26 10:35
 */
//@Configuration
public class FliterConfig {

	@Resource
	private MyFilter myFilter;

	@Bean
	public FilterRegistrationBean testFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(myFilter);
		registration.addUrlPatterns("/*");
		registration.setName("testFilter");
		registration.setOrder(1);
		return registration;
	}
}