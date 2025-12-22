package com.example.boot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;


/**
 * JWT 工具类
 *
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/21 10:41
 */
public class JwtTokenUtil {
	private static final String SECRET = "pwyjwtsecret";
	/**
	 * 过期时间，8个小时
	 */
	public static final long EXPIRATION = 8 * 60 * 60 * 1000;

	/**
	 * 创建token
	 *
	 * @param username 用户名
	 * @return
	 */
	public static String getToken(String username) {
		// 指定签名的时候使用的签名算法，也就是header那部分
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		// 设置jwt的body
		JwtBuilder builder = Jwts.builder()
				// 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setId(UUID.randomUUID().toString())
				// iat: jwt的签发时间
				.setIssuedAt(new Date(System.currentTimeMillis()))
				// 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串
				.setSubject(username)
				// 设置签名使用的签名算法和签名使用的秘钥
				.signWith(signatureAlgorithm, SECRET);
		if (EXPIRATION >= 0) {
			// 设置过期时间
			builder.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION));
		}
		return builder.compact();
	}


	public static boolean validateToken(String token) {
		return parseToken(token) != null;
	}

	public static Claims getClaims(String jwtStr) {
		Claims claims = null;
		try {
			claims = parseToken(jwtStr);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return claims;
	}

	/**
	 * 解析JWT字符串
	 *
	 * @param token
	 * @return
	 *
	 * @throws Exception
	 */
	public static Claims parseToken(String token) {
		Claims body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		return body;
	}

	/**
	 * 获取Subject
	 *
	 * @param token
	 * @return
	 *
	 * @throws Exception
	 */
	public static String getSubject(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * 验证token是否过期
	 *
	 * @param token
	 * @return
	 */
	public static boolean verifyTokenExpireDate(String token) {
		Date expirationDate;
		try {
			expirationDate = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getExpiration();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return expirationDate != null && new Date().before(expirationDate);
	}
}
