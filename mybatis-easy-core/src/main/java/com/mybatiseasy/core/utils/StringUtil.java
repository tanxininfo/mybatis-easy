package com.mybatiseasy.core.utils;

public class StringUtil {

    public static boolean isEmpty(String str){
        return (str== null) || str.isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return (str!= null) && !str.isEmpty();
    }

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

    /**
     * 用于sql语句中，给表或字段名添加`号，避免和sql关键字冲突
     * @param str 表名或列名
     * @return 加上`号的表名或列名
     */
    public static String addBackquote(String str){
        return str.startsWith("`")? str: "`"+str+"`";
    }


    /**
     * 用于sql语句中，给表或字段名去除`号
     * @param str 表名或列名
     * @return 去除`号的表名或列名
     */
    public static String removeBackquote(String str){
        return str.replace("`","");
    }

}
