/*
 *
 *  * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (soft@tanxin.info).
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.mybatiseasy.core.utils;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象和map的互相转换
 */
@Slf4j
public class ObjectUtil {
    /**
     * 把bean对象转换为Map<String, Object>
     * @param bean bean对象
     * @param isToUnderlineCase 是否转为下划线
     * @param ignoreNullValue 是否忽略null值
     * @return Map<String, Object>
     */
    public static Map<String, Object> beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
       return BeanUtil.beanToMap(bean, isToUnderlineCase, ignoreNullValue);
    }

    /**
     * 把Map<String, Object>转换为bean对象
     * @param source 原对象，如Map<String, Object>
     * @param clazz 目标对象类
     * @return  目标对象
     */
    public static <T> T mapToBean(Object source, Class<T> clazz) {
        return BeanUtil.toBean(source, clazz);
    }

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
            return null;
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

    @SuppressWarnings("unchecked")
    public static <T> List<T> objectToList(Object obj, Class<T> tClass){
        List<T> list = new ArrayList<T>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<T>) obj) {
                list.add(tClass.cast(o));
            }
            return list;
        }
        return null;
    }

    public static <K, V> List<Map<K, V>> objectToListMap(Object obj, Class<K> kCalzz, Class<V> vCalzz) {
        List<Map<K, V>> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object mapObj : (List<?>) obj) {
                if (mapObj instanceof Map<?, ?>) {
                    Map<K, V> map = new HashMap<>(16);
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) mapObj).entrySet()) {
                        map.put(kCalzz.cast(entry.getKey()), vCalzz.cast(entry.getValue()));
                    }
                    result.add(map);
                }
            }
            return result;
        }
        return null;
    }

    /**
     *
     * @param obj 对象
     * @return List<Map<String, Object>>
     */
    public static   List<Map<String, Object>> objectToListMap(Object obj){
        return objectToListMap(obj, String.class, Object.class);
    }


    public static boolean isEmpty(Object ...array){
        return (array== null) || array.length<=0;
    }

    public static boolean isNotEmpty(Object ...array){
        return !isEmpty(array);
    }
}
