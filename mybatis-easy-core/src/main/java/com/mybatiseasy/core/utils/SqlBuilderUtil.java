package com.mybatiseasy.core.utils;

import cn.hutool.json.JSONUtil;
import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 获得insert和update语句
 */
@Slf4j
public class SqlBuilderUtil {

    /**
     * 拼接insert字段
     * @param columnList List<EntityFieldMap>
     * @return 字段列表
     */
    private static String getInsertColumnSymbolList(List<EntityFieldMap> columnList) {
        return columnList.stream().map(EntityFieldMap::getColumn).collect(Collectors.joining(", "));
    }

    /**
     * 根据实体表获取insert语句 字段
     * @param map 参数
     * @param entityMap 实体映射
     * @param entityObj 传入的实体对象
     * @return List<String>
     */
    private static List<String> getInsertValues(Map<String, Object> map, EntityMap entityMap, MetaObject entityObj){
        List<String> valueList = new ArrayList<>();
        String name;
        Object value;
        String insertDefault;
        for (EntityFieldMap fieldMap:entityMap.getEntityFieldMapList()
        ) {
            name = fieldMap.getName();
            value = entityObj.getValue(name);
            if(value != null) {
                map.put(name, value);
                valueList.add(getColumnValue(fieldMap, name));
            }else{
                insertDefault = fieldMap.getInsertDefault();
                if(!insertDefault.isEmpty()){
                    map.put(name, insertDefault);
                    valueList.add(getColumnValue(fieldMap, name, insertDefault));
                }
            }
        }
        return valueList;
    }

    /**
     * 根据实体表获取批量insert语句 字段
     * @param map 参数
     * @param entityObj
     * @return
     */
    private static List<String> getInsertValues(Map<String, Object> map,  List<Object> entityList, List<EntityFieldMap> insertColumnList) {

        List<String> valueList = new ArrayList<>();
        String name;
        Object value;
        String insertDefault;

        for (int i = 0; i < entityList.size(); i++) {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (EntityFieldMap fieldMap : insertColumnList
            ) {
                MetaObject entityObj = MetaObjectUtil.forObject(entityList.get(i));
                value = entityObj.getValue(fieldMap.getName());
                name = fieldMap.getName() + "_" + i;

                if (value != null) {
                    map.put(name, value);
                    joiner.add(getColumnValue(fieldMap, name));
                } else {
                    insertDefault = fieldMap.getInsertDefault();
                    map.put(name, insertDefault);
                    joiner.add(getColumnValue(fieldMap, name, insertDefault));
                }
            }
            valueList.add(joiner.toString());
        }

        return valueList;
    }

    /**
     * 获取占位符
     * @param fieldMap 字段映射
     * @return String
     */
    private static String getColumnValue(EntityFieldMap fieldMap, String fieldName){
       return getColumnValue(fieldMap, fieldName, null);
    }



    /**
     * 获取占位符
     * @param fieldMap 字段映射
     * @param fieldName parameter参数名
     * @param value 一般用于自动填充
     * @return String
     */
    private static String getColumnValue(EntityFieldMap fieldMap, String fieldName, String value) {
        if (fieldMap.getTypeHandler() != UnknownTypeHandler.class)
            return "#{" + fieldName + ", typeHandler=" + fieldMap.getTypeHandler() + "}";
        else if (TypeUtil.isNotEmpty(value)) return value;
        return "#{" + fieldName + "}";
    }

    public static String getInsertSql(Map<String, Object> map, EntityMap entityMap){
        MetaObject entityObject = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));

        List<EntityFieldMap> insertColumnList = getInsertColumnList(entityMap, entityObject);
        List<String> insertValues = SqlBuilderUtil.getInsertValues(map, entityMap, entityObject);

        return "INSERT INTO" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                "("+ getInsertColumnSymbolList(insertColumnList) +")" + Sql.SPACE + "VALUES (" +String.join(", ", insertValues)+")";
    }

    private static List<EntityFieldMap> getInsertColumnList(EntityMap entityMap, MetaObject entityObj) {
        List<EntityFieldMap> columnList = new ArrayList<>();

        Object value;
        String insertDefault;

        for (EntityFieldMap fieldMap : entityMap.getEntityFieldMapList()
        ) {
            value = entityObj.getValue(fieldMap.getName());
            if (value != null) columnList.add(fieldMap);
            else {
                insertDefault = fieldMap.getInsertDefault();
                if (!insertDefault.isEmpty()) {
                    columnList.add(fieldMap);
                }
            }
        }
        return columnList;
    }


    public static String getInsertBatchSql(Map<String, Object> map, EntityMap entityMap){
        List<Object> entityList = (List<Object>) map.get(MethodParam.ENTITY_LIST);
        Assert.notEmpty(entityList, "实体不得为空");
        MetaObject entityObject = MetaObjectUtil.forObject(entityList.get(0));

        List<EntityFieldMap> insertColumnList = getInsertColumnList(entityMap, entityObject);
        List<String> insertValues = SqlBuilderUtil.getInsertValues(map,  entityList, insertColumnList);

        return "INSERT INTO" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                "("+ getInsertColumnSymbolList(insertColumnList) +")" + Sql.SPACE + "VALUES"+ Sql.SPACE +String.join(", ", insertValues);
    }
}
