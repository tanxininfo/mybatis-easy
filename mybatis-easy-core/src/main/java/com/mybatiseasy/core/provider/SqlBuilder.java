package com.mybatiseasy.core.provider;

import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.enums.StatementType;
import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 获得insert和update语句
 */
@Slf4j
public class SqlBuilder {


    private List<String> insertSymbolList = new ArrayList<>();
    private List<List<String>> insertValuesList = new ArrayList<>();

    private List<String> updateValueList = new ArrayList<>();

    /**
     * 拼接insert字段
     * @param columnList List<EntityFieldMap>
     * @return 字段列表
     */
    private List<String> getInsertColumnSymbolList(List<EntityFieldMap> columnList) {
        return columnList.stream().map(EntityFieldMap::getColumn).collect(Collectors.toList());
    }

    /**
     * 根据实体表获取insert语句 字段
     * @param map 参数
     * @param entityMap 实体映射
     * @param entityObj 传入的实体对象
     * @return List<String>
     */
    private List<String> getInsertValues(Map<String, Object> map, EntityMap entityMap, MetaObject entityObj){
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
     * @param map 上下文
     * @param entityList 实体列表
     * @param insertColumnList  插入的column字段
     * @return  List<(#{value1}, #{value2}, ...)>
     */
    private List<List<String>> getInsertValuesList(Map<String, Object> map,  List<Object> entityList, List<EntityFieldMap> insertColumnList) {

        List<List<String>> valuesList = new ArrayList<>();
        String name;
        Object value;
        String insertDefault;

        for (int i = 0; i < entityList.size(); i++) {
            List<String> joiner = new ArrayList<>();
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
            valuesList.add(joiner);
        }

        return valuesList;
    }

    /**
     * 获取占位符
     * @param fieldMap 字段映射
     * @return String
     */
    private String getColumnValue(EntityFieldMap fieldMap, String fieldName){
       return getColumnValue(fieldMap, fieldName, null);
    }



    /**
     * 获取占位符
     * @param fieldMap 字段映射
     * @param fieldName parameter参数名
     * @param value 一般用于自动填充
     * @return String
     */
    private String getColumnValue(EntityFieldMap fieldMap, String fieldName, String value) {
        if (fieldMap.getTypeHandler() != UnknownTypeHandler.class)
            return "#{" + fieldName + ", typeHandler=" + fieldMap.getTypeHandler() + "}";
        else if (TypeUtil.isNotEmpty(value)) return value;
        return "#{" + fieldName + "}";
    }

    /**
     * 构建insert语句需要的columns和values
     * @param map 上下文
     * @param entityMap  实体
     * @return ['columns', 'values']
     */
    public void generateInsertParts(Map<String, Object> map, EntityMap entityMap){
        MetaObject entityObject = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));

        List<EntityFieldMap> insertColumnList = getInsertColumnList(entityMap, entityObject);
        insertValuesList.add(getInsertValues(map, entityMap, entityObject));
        insertSymbolList = getInsertColumnSymbolList(insertColumnList);
    }

    public List<String> getInsertSymbolList(){
        return this.insertSymbolList;
    }

    public List<List<String>> getInsertValuesList(){
        return this.insertValuesList;
    }
    public List<String> getUpdateValueList(){
        return this.updateValueList;
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

    public  void generateInsertBatchParts(Map<String, Object> map, EntityMap entityMap){
        List<Object> entityList = (List<Object>) map.get(MethodParam.ENTITY_LIST);
        Assert.notEmpty(entityList, "实体不得为空");
        MetaObject entityObject = MetaObjectUtil.forObject(entityList.get(0));

        List<EntityFieldMap> insertColumnList = getInsertColumnList(entityMap, entityObject);
        insertValuesList = getInsertValuesList(map,  entityList, insertColumnList);
        insertSymbolList = getInsertColumnSymbolList(insertColumnList);
    }


    public   void generateUpdateParts(Map<String, Object> map, EntityMap entityMap) {
        MetaObject entityObject = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));

        List<String> valueList = new ArrayList<>();

        String name;
        Object value;
        String updateDefault;

        for (EntityFieldMap fieldMap : entityMap.getEntityFieldMapList()
        ) {
            //主键不参与更新
            if(fieldMap.getIsId()) continue;;

            name = fieldMap.getName();
            value = entityObject.getValue(name);

            if (value != null) {
                map.put(name, value);
                valueList.add(formatUpdateItem(fieldMap, name, ""));
            }
            else {
                updateDefault = fieldMap.getUpdateDefault();
                if (!updateDefault.isEmpty()) {
                    map.put(name, updateDefault);
                    valueList.add(formatUpdateItem(fieldMap, name, updateDefault));
                }
            }
        }
        this.updateValueList = valueList;
    }

    private String formatUpdateItem(EntityFieldMap fieldMap, String name, String value){
        return fieldMap.getColumn() + "=" + getColumnValue(fieldMap, name, value);
    }

}
