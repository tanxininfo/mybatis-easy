package com.mybatiseasy.core.utils;


import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String columnName = field.getName();
            Object value = map.get(field.getName());

            if (value != null && metaObject.hasGetter(columnName)) {
                metaObject.setValue(columnName, convertValue(value, metaObject.getGetterType(columnName)));
            }
        }

        return object;

    }

    private static Object convertValue(Object value, Class<?> toType){
        Class<?> fromType = value.getClass();
        log.info("fromType={}", fromType);
        log.info("toType={}", toType);
        if(fromType == BigInteger.class && toType == Long.class){
            return ((BigInteger)value).longValue();
        }
        else if(fromType == BigInteger.class && toType == Integer.class){
            return ((BigInteger)value).intValue();
        }
        return value;
    }
}
