package com.example.boot.Interceptor;


import com.example.boot.util.IpUtil;
import com.example.boot.util.JacksonUtil;
import com.example.boot.util.JwtTokenUtil;
import com.example.boot.util.LoginUserUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * JWT拦截器
 *
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/21 10:41
 */
//@Component
public class TokenInterceptor implements HandlerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getHeader("token");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		if (token == null) {
			response.getWriter().println(JacksonUtil.getJsonString(""));
			log.info("token不能为空");
			return false;
		}
		try {
			if (!JwtTokenUtil.validateToken(token)) {
				response.getWriter().println(JacksonUtil.getJsonString(""));
				log.info("token解析错误");
				return false;
			}
			if (LoginUserUtil.verifyLoginUser(IpUtil.getIpAddr(request), JwtTokenUtil.getSubject(token))) {
				response.getWriter().println(JacksonUtil.getJsonString("未登录，请先登录"));
				log.info("未登录，请先登录");
				return false;
			}
		} catch (ExpiredJwtException ex) {
			response.getWriter().println(JacksonUtil.getJsonString("登录信息已过期，请从新登录"));
			log.info("token过期需要重新登录");
			return false;
		} catch (Exception e) {
			response.getWriter().println(JacksonUtil.getJsonString(""));
			log.info("token解析错误:{}", e.getMessage());
			return false;
		}
		return true;
	}
}
