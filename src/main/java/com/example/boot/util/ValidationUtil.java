package com.example.boot.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/26 17:36
 */

public class ValidationUtil {

	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
}