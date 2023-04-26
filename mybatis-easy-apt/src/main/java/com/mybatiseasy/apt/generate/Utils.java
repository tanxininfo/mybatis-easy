package com.mybatiseasy.apt.generate;

public class Utils {

    public static boolean isEmpty(String str){
        return (str== null) || str.isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
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
     * 首字母小写
     * @param str 输入字符串
     * @return 如: myMoney
     */
    public static String uncapitalize(String str){
        if(isEmpty(str)) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
