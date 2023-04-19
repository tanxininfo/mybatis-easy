package com.mybatiseasy.core.utils;

import java.util.Collection;

public class TypeUtil {

    /**
     * 字符串数组以,号连接成长串
     * @param in 字符串数组
     * @return 拼接后的长串或null
     */
    public static String ArrayToDelimitedString(String[] in) {
        return in != null && in.length != 0 ? String.join(",", in): null;
    }

    /**
     * 长串以,号分割成字符串数组
     * @param in 长串
     * @return 分割成的字符串数组或null
     */
    public static String[] delimitedStringToArray(String in) {
        if (in == null || in.trim().length() == 0) {
            return null;
        }
        return in.split(",");
    }


    /**
     * 判断 Collection 是否为空
     *
     * @param collection Collection<?>
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断 Collection 是否不为空
     *
     * @param collection Collection<?>
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }


    public static boolean isEmpty(Object ...array){
        return (array== null) || array.length<=0;
    }

    public static boolean isNotEmpty(Object ...array){
        return !isEmpty(array);
    }


    public static boolean isEmpty(String str){
        return (str== null) || str.isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
