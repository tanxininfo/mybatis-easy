package com.mybatiseasy.core.utils;

public class SqlUtil {
    /**
     * 判断是否需要加括号，如果只有一个条件时，不用加括号。
     *
     * @param sb StringBuilder
     * @return boolean
     */
    public static boolean needBracket(StringBuilder sb) {
        if (sb.length() == 0) return false;
        return sb.indexOf("OR") > 0;
    }

    /**
     * 判断是否需要加括号，如果只有一个条件时，不用加括号。
     *
     * @param str String
     * @return boolean
     */
    public static boolean needBracket(String str) {
        if (str.length() == 0) return false;
        return str.indexOf("OR") > 0;
    }
}
