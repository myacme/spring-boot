package com.example.boot.Interceptor;


import com.example.boot.util.Base64Converter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;


/**
 * @author liujixiang
 */
//@ControllerAdvice
public class InterceptResponse implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Nullable
	@Override
	public Object beforeBodyWrite(@Nullable Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (isEncrypt(serverHttpRequest.getURI().toString())) {
            return body;
        }
        return Base64Converter.encode(body);
	}

    /**
     * 判断是否需要进行加解密操作
     *
     * @param url url
     * @return
     */
    public static boolean isEncrypt(String url) {
        // 以下环境不进行参数加解密
        List<String> host = List.of("localhost", "127.0.0.1", "192.168");
        // 白名单模式，不进行参数解密
        return host.stream().anyMatch(url::contains);
    }
}