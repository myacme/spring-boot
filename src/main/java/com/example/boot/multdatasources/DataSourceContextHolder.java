package com.example.boot.multdatasources;

/**
 * 线程数据源切换
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/12/22 13:33
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSourceKey(String dataSourceKey) {
        CONTEXT_HOLDER.set(dataSourceKey);
    }

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}
