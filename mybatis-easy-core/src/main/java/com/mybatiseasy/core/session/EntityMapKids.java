/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

package com.mybatiseasy.core.session;

import com.mybatiseasy.annotation.Table;
import com.mybatiseasy.annotation.TableField;
import com.mybatiseasy.annotation.TableId;
import com.mybatiseasy.core.utils.StringUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class EntityMapKids {

    /**
     * 存放实体类
     */
    private static final Map<String, EntityMap> entityMaps = new HashMap<>();

    /**
     * 添加EntityMap(实体类信息)
     * @param entityName 实休类名称
     * @param entityMap 实体类映射对象
     */
    public static void addEntityMap(String entityName, EntityMap entityMap) {
        entityMaps.put(entityName, entityMap);
    }

    /**
     * 获得实体类映射对象名称
     * @return 所有实体类映射对象名称(mapperNames)
     */
    public static Collection<String> getEntityMapNames() {
        return entityMaps.keySet();
    }

    /**
     * 获得所有实体类映射对象
     * @return  所有实体类映射对象
     */
    public static Collection<EntityMap> getEntityMaps() {
        return entityMaps.values();
    }

    /**
     * 通过实体类名称获得映射对象
     * @param entityName 实体类名称
     * @return 实体类映射对象
     */
    public static EntityMap getEntityMap(String entityName) {
        if (!hasEntityMap(entityName)) {
            EntityMap entityMap = EntityMapKids.reflectEntity(entityName);
            if (entityMap == null) return null;
            addEntityMap(entityName, entityMap);
        }
        return entityMaps.get(entityName);
    }

    /**
     * 通过mapperName获得映射对象
     * @param mapperName mapperName
     * @return 实体类映射对象
     */
    public static EntityMap getEntityMapByMapperName(String mapperName) {
        return getEntityMap(getEntityName(mapperName));
    }

    /**
     * 通过ProviderContext获得映射对象
     * @param context ProviderContext
     * @return 实体类映射对象
     */
    public static EntityMap getEntityMapByContext(ProviderContext context) {
        Class<?> mapperClass = context.getMapperType();
        return getEntityMapByMapperName(mapperClass.getName());
    }

    /**
     * 通过mapperName获得EntityName
     * @param mapperName mapperName
     * @return
     */
    public static String getEntityName(String mapperName) {
        try {
            Class<?> mapperClass = Class.forName(mapperName);
            String classType = mapperClass.getGenericInterfaces()[0].getTypeName();
            return classType.split("[<>]")[1];
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 判断是否已经有实体类映射对象
     * @param entityName 实休类名称
     * @return 是否包含
     */
    public static boolean hasEntityMap(String entityName) {
        return entityMaps.containsKey(entityName);
    }

    /**
     * 通过实体类结构取得实体类映射对象
     * @param entityName 实体类名称
     * @return 实体类映射对象
     */
    public static EntityMap reflectEntity(String entityName) {
        try {
            Class<?> entityClass = Class.forName(entityName);
            Table table = AnnotationUtils.findAnnotation(entityClass, Table.class);
            if (table == null) return null;

            EntityFieldMap primary = null;
           
            List<EntityFieldMap> entityFieldMapList = new ArrayList<>();
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                EntityFieldMap fieldMap = reflectEntityMap(field);
                if(fieldMap.getIsId()) primary = fieldMap;
                entityFieldMapList.add(fieldMap);
            }

            String tableName = TypeUtil.isEmpty(table.name())? StringUtil.camelToSnake(entityClass.getName()): table.name();
            return new EntityMap.Builder(tableName, table.desc()).schema(table.schema()).entityFieldMapList(entityFieldMapList)
                    .primary(primary)
                    .build();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 通过Field取得EntityMap属性
     * @param field 类属性
     * @return EntityFieldMap
     */
    public static EntityFieldMap reflectEntityMap(Field field) {
        TableField tableField = AnnotationUtils.findAnnotation(field, TableField.class);
        TableId tableId = AnnotationUtils.findAnnotation(field, TableId.class);
        String name = field.getName();
        String column = StringUtil.camelToSnake(field.getName());
        EntityFieldMap.Builder builder = new EntityFieldMap.Builder(name, column).javaType(field.getType());


        if (tableField != null) {
            builder.desc(tableField.desc()).insertDefault(tableField.insert())
                    .updateDefault(tableField.update()).jdbcType(tableField.jdbcType())
                    .isLarge(tableField.isLarge()).numericScale(tableField.numericScale())
                    .typeHandler(tableField.typeHandler());
            if (!tableField.column().isEmpty()) builder.column(tableField.column());
        }
        if (tableId != null) builder.isId(true).keyGenerator(tableId.keyGenerator()).sequence(tableId.sequence()).idType(tableId.idType());
        return builder.build();

    }
}
