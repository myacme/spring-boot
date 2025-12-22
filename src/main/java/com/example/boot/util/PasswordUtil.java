package com.example.boot.util;


import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/8/23 10:43
 */

public class PasswordUtil {

	public static final String KEY = "2022082361709055";

	/**
	 * crypto解密
	 *
	 * @param password
	 * @return
	 *
	 * @throws Exception
	 */
	public static String decryptAES(String password) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");
		// "算法/模式/补码方式"
		byte[] decode = Base64.getDecoder().decode(password);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return new String(cipher.doFinal(decode));
	}


	/**
	 * bcrypt比较
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static boolean decrypt(String loginPassword, String dataPassword) throws Exception {
		return BCrypt.checkpw(PasswordUtil.decryptAES(loginPassword), dataPassword);
	}


	public static void main(String[] args) throws Exception {
		System.out.println(decryptAES("tChD91NfDX16oajf03FfHA=="));
		System.out.println(new Random().nextInt(89999999) + 10000000);
	}
}