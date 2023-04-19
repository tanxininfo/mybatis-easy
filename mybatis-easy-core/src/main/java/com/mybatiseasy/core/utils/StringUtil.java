package com.mybatiseasy.core.utils;

public class StringUtil {

    /**
     * 驼峰命名法转为下划线命名
     *
     * @param str 驼峰命名格式
     * @return 下划线命名格式
     */
    public static String camelToSnake(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线命名转为小驼峰命名
     *
     * @param str 下划线命名格式
     * @return 驼峰命名格式
     */
    public static String snakeToCamel(String str) {
        if (!str.contains("_")) return str;
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        boolean underLineFound = false;
        sb.append(chars[0]);
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                underLineFound = true;
            } else {
                if (underLineFound) {
                    sb.append(Character.toUpperCase(c));
                    underLineFound = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

}
