package com.example.boot.aspect;

import com.example.boot.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author wuxiaoxiao
 * @version 1.0
 * @date 2023/6/30
 */
//@Component
//@Aspect
public class LogAop implements Ordered {

    @Resource
    private HttpServletRequest request;

    private static final String SUCCESS = "SUCCESS";
    private static final String CODE_ONE = "200";
    private static final String CODE_TWO = "500";
    private static final String CODE = "code";
    private static final String SUCCESS_CN = "成功";
    private static final String FAIL_CN = "失败";
    private static final String HIGH = "高";
    private static final String MIDDLE = "中";
    private static final String UNKNOWN = "unknown";
    private static final String LOCAL = "127.0.0.1";
    private static final String COMMA = ",";
    private static final String HOST = "0:0:0:0:0:0:0:1";
    private static final int FIFTEEN = 15;

    /**
     * 定义BusLogAop的切入点为标记@BusLog注解的方法
     */
    @Pointcut(value = "@annotation(com.example.boot.annotation.Log)")
    public void pointcut() {
    }

    /**
     * 业务操作环绕通知
     *
     * @param proceedingJoinPoint
     * @retur
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        //执行目标方法
        Object target = proceedingJoinPoint.getTarget();
        Object[] args = proceedingJoinPoint.getArgs();
        Object ret = null;
        try {
            ret = proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //目标方法执行完成后，获取目标类、目标方法上的业务日志注解上的功能名称和功能描述
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Log anno1 = target.getClass().getAnnotation(Log.class);
        Log anno2 = signature.getMethod().getAnnotation(Log.class);
        String clazzValue = anno1.value();
        String methodValue = anno2.value();
        String resultStr = "";
        String errorLevel = "";
        if (ret != null) {
            String code = getValueByKey(ret, CODE).toString();
            if (SUCCESS.equalsIgnoreCase(code) || CODE_ONE.equals(code)) {
                resultStr = SUCCESS_CN;
            } else if (CODE_TWO.equals(code)) {
                resultStr = FAIL_CN;
                errorLevel = HIGH;
            } else {
                resultStr = FAIL_CN;
                errorLevel = MIDDLE;
            }
        }
        //保存业务操作日志信息
        System.out.println(clazzValue + methodValue);
        return ret;
    }


    @Override
    public int getOrder() {
        return 1;
    }


    /**
     * 获取单个对象指定键的值
     *
     * @param t
     * @param key
     * @param <T>
     * @return
     */
    public static <T> Object getValueByKey(T t, String key) {
        Class clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Field resultField = Arrays.stream(fields)
                .filter(field -> field.getName().equals(key))
                .findFirst()
                .get();
        Object obj = null;
        resultField.setAccessible(true);
        try {
            obj = resultField.get(t);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL.equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > FIFTEEN) {
            if (ip.indexOf(COMMA) > 0) {
                ip = ip.substring(0, ip.indexOf(COMMA));
            }
        }
        if (HOST.equals(ip)) {
            ip = LOCAL;
        }
        return ip;
    }
}