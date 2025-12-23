package com.example.boot.bean;


import lombok.Data;

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

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6095755606671547258L;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String code;
    private String msg;
    private String time;
    private T result;

    public Result(String code, String msg, String time, T result) {
        this.code = code;
        this.msg = msg;
        this.time = time;
        this.result = result;
    }

    public Result(ResultStatus status, String time, T result) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.time = time;
        this.result = result;
    }

    public static <T> Result<T> ok() {
        return new Result<>(ResultStatus.SUCCESS, SIMPLE_DATE_FORMAT.format(new Date()), null);
    }

    public static <T> Result<T> ok(T result) {
        return new Result<>(ResultStatus.SUCCESS, SIMPLE_DATE_FORMAT.format(new Date()), result);
    }

    public static <T> Result<T> ok(String code, String msg, Date time, T result) {
        return new Result<>(code, msg, SIMPLE_DATE_FORMAT.format(time), result);
    }

    public static <T> Result<T> error() {
        return new Result<>(ResultStatus.INTERNAL_SERVER_ERROR, SIMPLE_DATE_FORMAT.format(new Date()), null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>("500", msg, SIMPLE_DATE_FORMAT.format(new Date()), null);
    }

    public static <T> Result<T> error(String code, String msg) {
        return new Result<>(code, msg, SIMPLE_DATE_FORMAT.format(new Date()), null);
    }

    public static <T> Result<T> error(T result) {
        return new Result<>(ResultStatus.INTERNAL_SERVER_ERROR, SIMPLE_DATE_FORMAT.format(new Date()), result);
    }

    public static <T> Result<T> error(ResultStatus status, T result) {
        return new Result<>(status, SIMPLE_DATE_FORMAT.format(new Date()), result);
    }

    public static <T> Result<T> error(ResultStatus status) {
        return new Result<>(status, SIMPLE_DATE_FORMAT.format(new Date()), null);
    }

    public static <T> Result<T> error(String code, String msg, Date time, T result) {
        return new Result<>(code, msg, SIMPLE_DATE_FORMAT.format(time), result);
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
}