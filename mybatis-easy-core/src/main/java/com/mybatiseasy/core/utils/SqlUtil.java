/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mybatiseasy.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

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
     *
     * @return 如： 'a', 1
     */
    public static String formatInElement(Object obj) {
        if (obj instanceof String) {
            return addSymbol(obj.toString(), "'", "'");
        } else return obj.toString();
    }


    /**
     * 用于sql语句中，给表或字段名添加`号，避免和sql关键字冲突
     *
     * @param str 表名或列名
     * @return 加上`号的表名或列名
     */
    public static String addBackquote(String str) {
        return str.startsWith("`") ? str : "`" + str + "`";
    }


    /**
     * 用于sql语句中，给表或字段名去除`号
     *
     * @param str 表名或列名
     * @return 去除`号的表名或列名
     */
    public static String removeBackquote(String str) {
        return str.replace("`", "");
    }


    /**
     * 用于sql语句中，给字符串加符号
     *
     * @param str String
     * @return 加上''的值
     */
    public static String addSymbol(String str, String leftSymbol, String rightSymbol) {
        return str.startsWith(leftSymbol) ? str : leftSymbol + str + rightSymbol;
    }


    /**
     * 用于sql语句中，给表或字段名去除符号
     *
     * @param str String
     * @return 去除''的值
     */
    public static String removeSymbol(String str, String leftSymbol, String rightSymbol) {
        return str.replace(leftSymbol, "").replace(rightSymbol, "");
    }

    public static String addSingQuote(String str) {
        return addSymbol(str, "'", "'");
    }


    public static String getMapKey(String column) {
        String originColumn = SqlUtil.removeBackquote(column);
        return originColumn + "_" + IdUtil.uniqueId().id();
    }

    public static String getMapKey(String column, int index) {
        String originColumn = SqlUtil.removeBackquote(column);
        return originColumn +"_" + index;
    }

    public static String getValueTag(String key) {
        return "#{" + key + "}";
    }
}
