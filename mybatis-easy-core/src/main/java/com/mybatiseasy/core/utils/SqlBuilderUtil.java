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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
     * @param entityMap
     * @param entityObj
     * @return
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
                valueList.add(getColumnValue(fieldMap));
            }else{
                insertDefault = fieldMap.getInsertDefault();
                if(!insertDefault.isEmpty()){
                    map.put(name, insertDefault);
                    valueList.add(getColumnValue(fieldMap, insertDefault));
                }
            }
        }
        return valueList;
    }

//    /**
//     * 根据实体表获取insert语句 字段
//     * @param entityMap
//     * @param entityObj
//     * @return
//     */
//    private static List<String> getInsertValues(Map<String, Object> map, EntityMap entityMap, MetaObject entityObj){
//        List<String> valueList = new ArrayList<>();
//        String name;
//        Object value;
//        String insertDefault;
//        for (EntityFieldMap fieldMap:entityMap.getEntityFieldMapList()
//        ) {
//            name = fieldMap.getName();
//            value = entityObj.getValue(name);
//            if(value != null) {
//                map.put(name, value);
//                valueList.add(getColumnValue(fieldMap));
//            }else{
//                insertDefault = fieldMap.getInsertDefault();
//                if(!insertDefault.isEmpty()){
//                    map.put(name, insertDefault);
//                    valueList.add(getColumnValue(fieldMap, insertDefault));
//                }
//            }
//        }
//        return valueList;
//    }

    /**
     * 获取占位符
     * @param fieldMap 字段映射
     * @return String
     */
    private static String getColumnValue(EntityFieldMap fieldMap){
       return getColumnValue(fieldMap, null);
    }

    /**
     * 获取占位符
     * @param fieldMap 字段映射
     * @param value 一般用于自动填充
     * @return String
     */
    private static String getColumnValue(EntityFieldMap fieldMap, String value) {
        if (fieldMap.getTypeHandler() != UnknownTypeHandler.class)
            return "#{" + fieldMap.getName() + ", typeHandler=" + fieldMap.getTypeHandler() + "}";
        else if (TypeUtil.isNotEmpty(value)) return value;
        return "#{" + fieldMap.getName() + "}";
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
//        List<Object> entityList = (List<Object>) map.get(MethodParam.ENTITY_LIST);
//        Assert.notEmpty(entityList, "实体不得为空");
//        MetaObject entityObject = MetaObjectUtil.forObject(entityList.get(0));
//
//        List<EntityFieldMap> insertColumnList = getInsertColumnList(entityMap, entityObject);
//        List<String> insertValues = SqlBuilderUtil.getInsertValues(map, entityMap, entityObject);
//
//        return "INSERT INTO" + Sql.SPACE +
//                entityMap.getName() + Sql.SPACE +
//                "("+ getInsertColumnSymbolList(insertColumnList) +")" + Sql.SPACE + "VALUES (" +String.join(", ", insertValues)+")";
        return "";
    }
}
