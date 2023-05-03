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




    public static boolean isEmpty(String str){
        return (str== null) || str.isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }




}
