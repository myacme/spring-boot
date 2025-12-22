package com.example.boot.config;


import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * json序列化配置
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/8/1 下午1:20
 */
@Configuration
public class JacksonConfig {

    /**
     * json序列化配置  Long -> String  解决雪花算法id精度丢失问题 number精度丢失
     * <p>
     * json序列化  格式化时间
     * <p>
     * <p>
     * yml：
     * spring:
     * jackson:
     * date-format: yyyy-MM-dd HH:mm:ss
     * time-zone: GMT+8
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder ->
                //Long -> String
                builder.serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance)
                        //格式化date
                        .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                        .timeZone(TimeZone.getTimeZone("GMT+8"))
                        //格式化LocalDateTime java8
                        .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
