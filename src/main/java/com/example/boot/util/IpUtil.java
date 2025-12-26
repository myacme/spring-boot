package com.example.boot.util;


import javax.servlet.http.HttpServletRequest;

/**
 * IP地址工具类
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/19 16:42
 */
public class IpUtil {
    private IpUtil() {
        throw new UnsupportedOperationException("工具类不能实例化");
    }

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    /**
     * 获取客户端真实IP地址
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 对于 X-Forwarded-For，取第一个非unknown的IP
                if ("X-Forwarded-For".equalsIgnoreCase(header)) {
                    int index = ip.indexOf(',');
                    if (index != -1) {
                        ip = ip.substring(0, index);
                    }
                }
                return ip.trim();
            }
        }

        // 如果所有header都为空，使用remoteAddr
        String remoteAddr = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            remoteAddr = "127.0.0.1";
        }
        return remoteAddr;
    }

    /**
     * 获取客户端真实IP地址（带多级代理支持）
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    public static String getClientIpWithMultiProxy(HttpServletRequest request) {
        String ip = null;

        // 1. 优先处理 X-Forwarded-For
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            String[] ips = xff.split(",");
            for (String proxyIp : ips) {
                proxyIp = proxyIp.trim();
                if (isValidIp(proxyIp)) {
                    ip = proxyIp;
                    break;
                }
            }
        }

        // 2. 其他header
        if (!isValidIp(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        // 3. 常规header
        if (!isValidIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (!isValidIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (!isValidIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (!isValidIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        // 4. 最后使用remoteAddr
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理本地地址
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    /**
     * 验证IP地址格式
     *
     * @param ip IP地址字符串
     * @return 是否为有效的IP地址格式
     */
    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 验证IP地址格式（支持IPv4和IPv6）
     *
     * @param ip IP地址字符串
     * @return 是否为有效的IP地址格式
     */
    public static boolean isValidIpAddress(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        // IPv4 正则
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
                + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        // IPv6 正则（简化版）
        String ipv6Pattern = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";

        return ip.matches(ipv4Pattern) || ip.matches(ipv6Pattern);
    }
}
