package com.example.boot.util;


import com.example.boot.bean.LoginUser;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/8/31 17:40
 */

public class LoginUserUtil {

	public static String getLoginUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			String token = request.getHeader("token");
			return JwtTokenUtil.getSubject(token);
		}
		return null;
	}

	public static boolean verifyLoginUser(String ip, String username) {
		return !ip.equals(LoginUser.LOGON_USER_MAP.get(username));
	}
}