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

package com.mybatiseasy.generator.utils;

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
     * 首字母小写
     * @param str 输入字符串
     * @return 如: myMoney
     */
    public static String uncapitalize(String str){
        if(isEmpty(str)) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }


    /**
     * 首字母小写
     * @param str 输入字符串
     * @return 如: myMoney
     */
    public static String capitalize(String str){
        if(isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
