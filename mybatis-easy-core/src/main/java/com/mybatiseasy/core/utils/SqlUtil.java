package com.mybatiseasy.core.utils;

import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Slf4j
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

    /**
     * 格式化成in表达式
     *
     * @param array 数组
     * @return in表达式
     */
    public static Object[] formatArray(Object[] array) {
        if (ObjectUtil.isEmpty(array)) {
            return null;
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = SqlUtil.formatInElement(array[i]);
        }
        return array;
    }

    /**
     * 格式化成in表达式
     *
     * @param value Collection
     * @return in表达式
     */
    public static List<?> formatArray(Collection<?> value) {
        if (CollectionUtil.isEmpty(value)) {
            return null;
        }
        return value.stream().map(SqlUtil::formatInElement).collect(Collectors.toList());
    }

    /**
     * 格式化in表达式里的项
     * @return 如： 'a', 1
     */
    public static String formatInElement(Object obj){
        if(obj instanceof String){
            return addSymbol(obj.toString(), "'", "'");
        } else return obj.toString();
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


    /**
     * 用于sql语句中，给字符串加符号
     * @param str String
     * @return 加上''的值
     */
    public static String addSymbol(String str, String leftSymbol, String rightSymbol){
        return str.startsWith(leftSymbol)? str: leftSymbol+str+rightSymbol;
    }


    /**
     * 用于sql语句中，给表或字段名去除符号
     * @param str String
     * @return 去除''的值
     */
    public static String removeSymbol(String str, String leftSymbol, String rightSymbol){
        return str.replace(leftSymbol,"").replace(rightSymbol, "");
    }

    public static String addSingQuote(String str){
        return addSymbol(str, "'", "'");
    }

    public static QueryWrapper initWrapper(QueryWrapper wrapper, String entityName) {
        if (!wrapper.hasSelect()) wrapper.select("*");
        if (!wrapper.hasTable()) wrapper.from(new Column(entityName));
        return wrapper;
    }


    public static QueryWrapper initDeleteWrapper(QueryWrapper wrapper, String entityName) {
        if (!wrapper.hasTable()) wrapper.deleteFrom(new Column(entityName));
        return wrapper;
    }
}
