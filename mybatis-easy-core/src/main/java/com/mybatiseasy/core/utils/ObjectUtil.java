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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 对象和map的互相转换
 */
@Slf4j
public class ObjectUtil {

    /**
     * 对象转字符串(json)
     * @param object 对象
     * @return json字符串
     */
    public static <T> String toJson(T object){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);


        try {
            return mapper.writeValueAsString(object);
        }catch (Exception e){
            throw new RuntimeException("toJson failed:"+e.getMessage());
        }
    }

    /**
     * 对象转字符串(json)
     * @param jsonStr json字符串
     * @return Map<String, Object>
     */
    public static Map<String, Object> strToMap(String jsonStr){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            return mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() { });
        }catch (Exception e){
           return null;
        }
    }

    public static boolean isEmpty(Object ...array){
        return (array== null) || array.length<=0;
    }

    public static boolean isNotEmpty(Object ...array){
        return !isEmpty(array);
    }
}
