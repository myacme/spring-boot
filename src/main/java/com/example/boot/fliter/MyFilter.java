package com.example.boot.fliter;


import com.example.boot.util.Base64Converter;
import com.example.boot.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 〈〉
 *
 * @author ljx
 * @version 1.0.0
 * @create 2021/11/26 10:31
 */
//@Component
//@WebFilter(filterName = "paramFilter",urlPatterns = "/*")
//@Order(1)
public class MyFilter implements Filter {

	private static final ObjectMapper JACKSON = new ObjectMapper();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("过滤器启用！！！！！！！！！！！！！！！！！");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		ParamHttpRequest requestWrapper = new ParamHttpRequest(request);
		String body = requestWrapper.getBody();
		Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
		if (!StringUtils.isEmpty(body)) {
			// 对参数进行处理
			String decode = Base64Converter.decode(body);
			// 由于body是设置成不可变的，所以需要重新创建一个request，将body设置进去
			requestWrapper = new ParamHttpRequest(requestWrapper, JACKSON.writeValueAsBytes(decode));
		}
		if (!CollectionUtils.isEmpty(parameterMap)) {
			// 对参数进行处理
			Map<String, String[]> decode = Base64Converter.decode(parameterMap);
			// 由于body是设置成不可变的，所以需要重新创建一个request，将body设置进去
			requestWrapper = new ParamHttpRequest(requestWrapper, decode);
		}
		filterChain.doFilter(requestWrapper, servletResponse);
	}

	@Override
	public void destroy() {
		System.out.println("过滤器注销！！！！！！！！！！！！！！！！！");
	}
}