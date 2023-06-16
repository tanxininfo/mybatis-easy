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

package com.mybatiseasy.core.config;

import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.Map;

public class GlobalConfig {
    private static ITenant tenantFactory;

    private static  final  Map<Class<?>, Map<Object, ? extends Enum<?>>> enumTypeMapList = new HashMap<>();
    private static  final  Map<Class<?>, Map<String, Object>> enumValueMapList = new HashMap<>();

    private static SqlSessionFactory sqlSessionFactory;

    public static ITenant getTenantFactory() {
        return tenantFactory;
    }

    public static void setTenantFactory(ITenant tenantFactory) {
        GlobalConfig.tenantFactory = tenantFactory;
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        GlobalConfig.sqlSessionFactory = sqlSessionFactory;
    }

    public static boolean existsEnumTypeMap(Class<?> clazz){
        return enumTypeMapList.containsKey(clazz);
    }

    public static Map<Object, ? extends Enum<?>> getEnumTypeMap(Class<?> clazz) {
        return enumTypeMapList.get(clazz);
    }

    public static void addEnumTypeMap(Class<?> clazz, Map<Object, ? extends Enum<?>> enumTypeMap) {
        GlobalConfig.enumTypeMapList.put(clazz, enumTypeMap);
    }

    public static boolean existsEnumValueMap(Class<?> clazz){
        return enumValueMapList.containsKey(clazz);
    }

    public static Map<String, Object> getEnumValueMap(Class<?> clazz) {
        return enumValueMapList.get(clazz);
    }

    public static void addEnumValueMap(Class<?> clazz, Map<String, Object> enumValueMap) {
        GlobalConfig.enumValueMapList.put(clazz, enumValueMap);
    }
}
