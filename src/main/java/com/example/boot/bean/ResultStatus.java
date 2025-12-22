package com.example.boot.bean;


/**
 * 〈〉
 *
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/21 11:01
 */

public enum ResultStatus {

	SUCCESS("200", "成功"),
	INTERNAL_SERVER_ERROR("500", "内部服务器错误"),
	PARAM_ERROR("400", "参数异常"),
	LOGIN_FAIL("401", "登录失败，请重新输入认证信息"),
	NO_AUTH("401", "认证失败，请登录后再访问"),
	AUTH_FAIL("401", "认证失败，无权访问"),
	NO_PERMISSION("403", "认证失败，没有权限"),
	NOT_FOUND("404", "找不到资源"),
	SIGN_ENCODER_ERROR("5001", "签名错误"),
	SIGN_VERIFY_ERROR("5002", "验签错误"),
	JSON_ERROR("5003", "JSON转换异常"),
	DB_ERROR("5004", "数据库操作异常"),
	DATA_NOT_FOUND("5007", "数据未找到");

	private final String code;
	private final String msg;

	ResultStatus(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResultStatus." + this.name() + "(code=" + this.getCode() + ", msg=" + this.getMsg() + ")";
	}

	public String getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}
}