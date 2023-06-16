package com.mybatiseasy.core.utils;


import com.mybatiseasy.core.session.EntityField;
import com.mybatiseasy.core.session.Entity;
import com.mybatiseasy.core.session.EntityKids;
import org.apache.ibatis.reflection.MetaObject;

import java.util.*;


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
        Entity entity = EntityKids.getEntityMap(entityClass.getName());
        assert entity != null;
        for (EntityField field : entity.getEntityFieldMapList()
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
