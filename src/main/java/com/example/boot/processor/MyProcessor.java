package com.example.boot.processor;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2023/2/15 11:17
 */
@Component
public class MyProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//		System.out.println("postProcessBefore: " + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//		System.out.println("postProcessAfter: " + beanName);
		return bean;
	}
}