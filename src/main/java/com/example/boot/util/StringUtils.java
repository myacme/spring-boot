package com.example.boot.util;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * string 工具类
 *
 * @author ljx
 * @version 1.0.0
 * @create 2022/7/21 10:41
 */
public class StringUtils {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###################.###########");

    private StringUtils() {
        // 防止通过反射创建实例
        throw new UnsupportedOperationException("StringUtils 是工具类，不能实例化");
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty() || "null".equals(str);
    }

    /**
     * 判断字符串对象是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isEmpty(Object str) {
        return str == null || str.toString().trim().isEmpty() || "null".equals(str);
    }


    /**
     * 有一个为空则返回true
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isAnyEmpty(Object... str) {
        for (Object o : str) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串对象是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }


    public static boolean equals(Object str1, Object str2) {
        return String.valueOf(str1).equals(String.valueOf(str2));
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 将逗号分隔的字符串格式化成"'"包裹切逗号分隔的字符串
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String formatString(String str) {
        String[] split = str.split(",");
        String[] temp = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            temp[i] = "'" + split[i] + "'";
        }
        return String.join(",", temp);
    }


    /**
     * 将逗号分隔的字符串格式化成"'"包裹切逗号分隔的字符串
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String formatString(List<String> str) {
        String[] temp = new String[str.size()];
        for (int i = 0; i < str.size(); i++) {
            temp[i] = "'" + str.get(i) + "'";
        }
        return String.join(",", temp);
    }


    /**
     * object 转字符串
     *
     * @param str
     * @return
     */
    public static String getString(Object str) {
        String a = "null";
        if (StringUtils.isEmpty(str)) {
            return "";
        } else if (a.equals(String.valueOf(str))) {
            return "";
        } else {
            return str.toString();
        }
    }

    /**
     * int 转字符串 为0显示为空
     *
     * @param i
     * @return
     */
    public static String getString(int i) {
        return i == 0 ? "" : String.valueOf(i);
    }

    /**
     * double 去掉末尾的0 为0.0显示为空
     *
     * @param d double
     * @return string
     */
    public static String getString(double d) {
        return d == 0 ? "" : DECIMAL_FORMAT.format(d);
    }

    /**
     * string转int
     *
     * @param object
     * @return int
     */
    public static int getIntDefault0(Object object) {
        try {
            return isEmpty(object) ? 0 : Integer.parseInt(getString(object));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * string转Double
     *
     * @param object
     * @return int
     */
    public static Double getDoubleDefault0(Object object) {
        try {
            return isEmpty(object) ? 0 : Double.parseDouble(getString(object));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * string转int
     *
     * @param object
     * @return int
     */
    public static int getIntDefault1(Object object) {
        try {
            return isEmpty(object) ? 1 : Integer.parseInt(getString(object));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
