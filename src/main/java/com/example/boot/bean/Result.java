package com.example.boot.bean;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈〉返回体实体类
 *
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/21 10:56
 */

public class Result implements Serializable {
	private static final long serialVersionUID = 6095755606671547258L;
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String code;
	private String msg;
	private String time;
	private Object result;

	public Result(String code, String msg, String time, Object result) {
		this.code = code;
		this.msg = msg;
		this.time = time;
		this.result = result;
	}

	public Result(ResultStatus status, String time, Object result) {
		this.code = status.getCode();
		this.msg = status.getMsg();
		this.time = time;
		this.result = result;
	}

	public static Result ok() {
		return new Result(ResultStatus.SUCCESS, SIMPLE_DATE_FORMAT.format(new Date()), null);
	}

	public static Result ok(Object result) {
		return new Result(ResultStatus.SUCCESS, SIMPLE_DATE_FORMAT.format(new Date()), result);
	}

	public static Result ok(String code, String msg, Date time, Object result) {
		return new Result(code, msg, SIMPLE_DATE_FORMAT.format(time), result);
	}

	public static Result error() {
		return new Result(ResultStatus.INTERNAL_SERVER_ERROR, SIMPLE_DATE_FORMAT.format(new Date()), null);
	}

	public static Result error(String msg) {
		return new Result("500", msg, SIMPLE_DATE_FORMAT.format(new Date()), null);
	}

	public static Result error(String code, String msg) {
		return new Result(code, msg, SIMPLE_DATE_FORMAT.format(new Date()), null);
	}

	public static Result error(Object result) {
		return new Result(ResultStatus.INTERNAL_SERVER_ERROR, SIMPLE_DATE_FORMAT.format(new Date()), result);
	}

	public static Result error(ResultStatus status, Object result) {
		return new Result(status, SIMPLE_DATE_FORMAT.format(new Date()), result);
	}

	public static Result error(ResultStatus status) {
		return new Result(status, SIMPLE_DATE_FORMAT.format(new Date()), null);
	}

	public static Result error(String code, String msg, Date time, Object result) {
		return new Result(code, msg, SIMPLE_DATE_FORMAT.format(time), result);
	}

	@Override
	public String toString() {
		return "Result{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				", time='" + time + '\'' +
				", result=" + result +
				'}';
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}