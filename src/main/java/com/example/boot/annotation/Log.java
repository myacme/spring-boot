package com.example.boot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2024/11/27 上午10:56
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 功能名称
     * @return
     */
    String value() default "";
}