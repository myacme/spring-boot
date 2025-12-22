package com.example.boot.util;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2023/4/19 17:34
 */

public class SerializeUtil {

	private SerializeUtil() {
	}


	/**
	 * 序列化
	 *
	 * @param obj
	 * @return
	 *
	 * @throws IOException
	 */
	public static byte[] serialize(Object obj) {
		try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		     ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOut)) {
			objectOutputStream.writeObject(obj);
			return byteOut.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 反序列化
	 *
	 * @param bytes
	 * @return
	 *
	 * @throws IOException
	 */
	public static Object deserialize(byte[] bytes) {
		try (ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
		     ObjectInputStream in = new ObjectInputStream(byteIn)) {
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		HashMap<Object, Object> map = new HashMap(4) {{
			put("11", 11);
		}};
		byte[] serialize = serialize(map);
		Object deserialize = deserialize(serialize);
		System.out.println((Map<String, Object>) deserialize);
	}
}