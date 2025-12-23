package com.example.boot.multdatasources;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 数据源拦截器
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/22 14:35
 */
@Component
public class DataSourceInterceptor implements HandlerInterceptor {

    
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 从请求中获取用户信息并设置数据源
        String dbName = extractUserDbFromRequest(request);
        DataSourceContextHolder.setDataSourceKey(dbName);
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, 
                              HttpServletResponse response, 
                              Object handler, Exception ex) {
        // 清理线程本地变量
        DataSourceContextHolder.clearDataSourceKey();
    }
    
    private String extractUserDbFromRequest(HttpServletRequest request) {
        // header中获取 db
        Enumeration<String> headerNames = request.getHeaderNames();
        return request.getHeader("db");
    }
}
