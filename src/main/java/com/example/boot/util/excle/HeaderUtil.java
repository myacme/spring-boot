package com.example.boot.util.excle;

import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 设置响应头
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/9/1 15:22
 */
public class HeaderUtil {

    private HeaderUtil() {
        throw new UnsupportedOperationException("工具类禁止实例化");
    }

    public static void setResponseHeaders(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.setContentType("application/octet-stream;charset=UTF-8");
    }

    public static void setResponseZip(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.setContentType("application/zip");
    }
}
