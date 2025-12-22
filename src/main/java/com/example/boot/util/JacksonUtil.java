package com.example.boot.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson工具类
 *
 * @author ljx
 * @create 2021/8/25 16:26
 * @since 1.0.0
 */

public class JacksonUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private JacksonUtil() {
	}

	/**
	 * 获取ObjectMapper
	 *
	 * @return ObjectMapper
	 */
	public static ObjectMapper getObjectMapper() {
		return MAPPER;
	}


	/**
	 * 获取json字符串
	 *
	 * @return string
	 */
	public static String getJsonString(Object object) throws JsonProcessingException {
		return MAPPER.writeValueAsString(object);
	}

	/**
	 * 获取JsonNode
	 *
	 * @param json json字符串
	 * @return JsonNode
	 *
	 * @throws JsonProcessingException
	 */
	public static JsonNode getJsonNode(String json) throws JsonProcessingException {
		return MAPPER.readTree(json);
	}

	public synchronized static <T> T json2GenericObject(String jsonString, TypeReference<T> tr) {
		if (jsonString != null && !("".equals(jsonString))) {
			try {
				return (T) (tr.getType().equals(String.class) ? jsonString : MAPPER.readValue(jsonString, tr));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
