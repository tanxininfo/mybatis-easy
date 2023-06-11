package com.mybatiseasy.core.utils;


import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.type.Record;
import com.mybatiseasy.core.type.RecordList;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class EntityMapUtil { 
     
    /**
     * map转对象
     * @param map Map
     * @param entityClass 对象类
     * @return 对象
     * @param <T> 对象类型
     * @throws Exception ex
     */
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> entityClass) throws Exception {

        T object = entityClass.getDeclaredConstructor().newInstance();
        MetaObject metaObject = MetaObjectUtil.forObject(object);
        EntityMap entityMap = EntityMapKids.getEntityMap(entityClass.getName());
        assert entityMap != null;
        for (EntityFieldMap field : entityMap.getEntityFieldMapList()
        ) {
            // 数据库字段
            String columnName = SqlUtil.removeBackquote(field.getColumn());
            // entity字段
            String fieldName = field.getName();
            Object value = map.get(columnName);

            if (value != null && metaObject.hasGetter(fieldName)) {
                Object convertedValue = ConversionUtil.convertValue(value, field.getJavaType());
                metaObject.setValue(fieldName, convertedValue);
            }
        }

        return object;

    }

    public static <T> List<T> mapToEntityList(List<Map<String, Object>> mapList, Class<T> entityClass) throws Exception {
        List<T> objList = new ArrayList<>();
        for (Map<String, Object> item: mapList
        ) {
            objList.add(mapToEntity(item, entityClass));
        }
        return objList;
    }
}
