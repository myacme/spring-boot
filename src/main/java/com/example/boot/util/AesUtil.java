package com.example.boot.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2023/6/25 15:16
 */

public class AesUtil {

	public static final String KEY = "2022082361709055";
	static final Base64.Encoder ENCODER = Base64.getEncoder();
	static final Base64.Decoder DECODER = Base64.getDecoder();
	private static final ObjectMapper JACKSON = new ObjectMapper();


	public static String encrypt(String result) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return ENCODER.encodeToString(cipher.doFinal(result.getBytes(StandardCharsets.UTF_8)));
	}

	public static String encrypt(Object result) throws Exception {
		//Object转json类型String
		String text = JACKSON.writeValueAsString(result);
		return encrypt(text);
	}

	public static String decrypt(String strToDecrypt) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(DECODER.decode(strToDecrypt)));
	}

	public static String[] decrypt(String[] param) throws Exception {
		if (param == null) {
			return null;
		}
		String[] param1 = new String[param.length];
		for (int i = 0; i < param.length; i++) {
			param1[i] = decrypt(param[i]);
		}
		return param1;
	}

	public static Map<String, String[]> decrypt(Map<String, String[]> param) throws Exception {
		if (param == null) {
			return null;
		}
		Set<String> keySet = param.keySet();
		for (String key : keySet) {
			param.put(key, decrypt(param.get(key)));
		}
		return param;
	}
}