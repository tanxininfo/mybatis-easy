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

package com.mybatiseasy.core.provider;

import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.enums.StatementType;
import com.mybatiseasy.core.session.EntityField;
import com.mybatiseasy.core.session.Entity;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.type.Record;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import com.mybatiseasy.core.utils.SqlUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获得insert和update语句
 */
public class SqlBuilder {


    private List<String> insertSymbolList = new ArrayList<>();
    private List<List<String>> insertValuesList = new ArrayList<>();

    private List<String> updateValueList = new ArrayList<>();

    /**
     * 拼接insert字段
     * @param columnList List<EntityFieldMap>
     * @return 字段列表
     */
    private List<String> getInsertColumnSymbolList(List<EntityField> columnList) {
        return columnList.stream().map(EntityField::getColumn).collect(Collectors.toList());
    }

    /**
     * 根据实体表获取insert语句 字段
     * @param map 参数
     * @param entity 实体映射
     * @param entityObj 传入的实体对象
     * @return List<String>
     */
    private List<String> getInsertValues(Map<String, Object> map, Entity entity, MetaObject entityObj){
        List<String> valueList = new ArrayList<>();
        String name;
        Object value;
        String insertDefault;
        for (EntityField fieldMap: entity.getEntityFieldMapList()
        ) {
            name = fieldMap.getName();
            value = null;
            if(entityObj.hasGetter(name)) value = entityObj.getValue(name);
            else{
                String otherName = SqlUtil.removeBackquote(fieldMap.getColumn());
                if(entityObj.hasGetter(otherName)) value = entityObj.getValue(otherName);
            }

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
    private List<List<String>> getInsertValuesList(Map<String, Object> map,  List<Object> entityList, List<EntityField> insertColumnList) {

        List<List<String>> valuesList = new ArrayList<>();
        String name;
        Object value;
        String insertDefault;
        String key;

        for (int i = 0; i < entityList.size(); i++) {
            List<String> joiner = new ArrayList<>();
            for (EntityField fieldMap : insertColumnList
            ) {
                MetaObject entityObj = MetaObjectUtil.forObject(entityList.get(i));
                key = fieldMap.getName();
                value = null;
                if(entityObj.hasGetter(key)) value = entityObj.getValue(key);
                else{
                    key = SqlUtil.removeBackquote(fieldMap.getColumn());
                    if(entityObj.hasGetter(key)) value = entityObj.getValue(key);
                }
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
    private String getColumnValue(EntityField fieldMap, String fieldName){
       return getColumnValue(fieldMap, fieldName, null);
    }



    /**
     * 获取占位符
     * @param fieldMap 字段映射
     * @param fieldName parameter参数名
     * @param value 一般用于自动填充
     * @return String
     */
    private String getColumnValue(EntityField fieldMap, String fieldName, String value) {
        if (fieldMap.getTypeHandler() != UnknownTypeHandler.class)
            return "#{" + fieldName + ", typeHandler=" + fieldMap.getTypeHandler() + "}";
        else if (TypeUtil.isNotEmpty(value)) return value;
        return "#{" + fieldName + "}";
    }

    /**
     * 构建insert语句需要的columns和values
     * @param map 上下文
     * @param entity  实体
     * @return ['columns', 'values']
     */
    public void generateInsertParts(Map<String, Object> map, Entity entity){
        generateInsertParts(map, entity, MethodParam.ENTITY);
    }


    /**
     * 构建insert语句需要的columns和values
     * @param map 上下文
     * @param entity  实体
     * @return ['columns', 'values']
     */
    public void generateInsertParts(Map<String, Object> map, Entity entity, String entityKey){
        MetaObject entityObject = MetaObjectUtil.forObject(map.get(entityKey));

        List<EntityField> insertColumnList = getInsertColumnList(entity, entityObject);
        insertValuesList.add(getInsertValues(map, entity, entityObject));
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



    private static List<EntityField> getInsertColumnList(Entity entity, MetaObject entityObj) {
        List<EntityField> columnList = new ArrayList<>();

        Object value;
        String key;
        String insertDefault;

        for (EntityField fieldMap : entity.getEntityFieldMapList()
        ) {
            key = fieldMap.getName();
            value = null;
            if(entityObj.hasGetter(key)) value = entityObj.getValue(key);
            else{
                key = SqlUtil.removeBackquote(fieldMap.getColumn());
                if(entityObj.hasGetter(key)) value = entityObj.getValue(key);
            }
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

    @SuppressWarnings("unchecked")
    public  void generateInsertBatchParts(Map<String, Object> map, Entity entity){
        generateInsertBatchParts(map, entity, MethodParam.ENTITY_LIST);
    }

    @SuppressWarnings("unchecked")
    public  void generateInsertBatchParts(Map<String, Object> map, Entity entity, String entityKey){
        List<Object> entityList = (List<Object>) map.get(entityKey);
        if(entityList.size()<=0) throw new RuntimeException("实体不得为空");

        MetaObject entityObject = MetaObjectUtil.forObject(entityList.get(0));

        List<EntityField> insertColumnList = getInsertColumnList(entity, entityObject);
        insertValuesList = getInsertValuesList(map,  entityList, insertColumnList);
        insertSymbolList = getInsertColumnSymbolList(insertColumnList);
    }

    public   void generateUpdateParts(Map<String, Object> map, Entity entity) {
        generateUpdateParts(map, entity, MethodParam.ENTITY);
    }

    public   void generateUpdateParts(Map<String, Object> map, Entity entity, Object entityKey) {
        MetaObject entityObject = null;
        Record record = null;
        if(entityKey.equals(MethodParam.RECORD)) {
            record = (Record)map.get(entityKey.toString());
        }else{
            entityObject = MetaObjectUtil.forObject(map.get(entityKey.toString()));
        }

        List<String> valueList = new ArrayList<>();

        String name;
        Object value;
        String updateDefault;

        for (EntityField fieldMap : entity.getEntityFieldMapList()
        ) {
            //主键不参与更新
            if (fieldMap.isId()) continue;

            name = entityKey.equals(MethodParam.RECORD)? SqlUtil.removeBackquote(fieldMap.getColumn()): fieldMap.getName();
            value = (entityObject != null)? entityObject.getValue(name): record.get(name);

            // 只有本数据表字段需要更新,isForeign表示非本数据表字段,比如多表连接时，其他表字段。
            if (!fieldMap.isForeign()) {
                if (value != null) {
                    map.put(name, value);
                    valueList.add(formatUpdateItem(fieldMap, name, ""));
                } else {
                    updateDefault = fieldMap.getUpdateDefault();
                    if (!updateDefault.isEmpty()) {
                        map.put(name, updateDefault);
                        valueList.add(formatUpdateItem(fieldMap, name, updateDefault));
                    }
                }
            }
        }
        this.updateValueList = valueList;
    }

    private String formatUpdateItem(EntityField fieldMap, String name, String value) {
        String columValue = fieldMap.isVersion() ? fieldMap.getColumn() + " + 1" : getColumnValue(fieldMap, name, value);
        return fieldMap.getColumn() + "=" + columValue;
    }

    /**
     * 生成批量更新的sql
     * @param map 上下文
     * @param entity  实体
     * @return String
     */
    @SuppressWarnings("unchecked")
    public String generateUpdateByIdBatchSql(Map<String, Object> map, Entity entity) {
        List<Object> entityList = (List<Object>) map.get(MethodParam.ENTITY_LIST);
        if(entityList.size()<=0) throw new RuntimeException("实体不得为空");
        List<String> sqlList = new ArrayList<>();
        int index = 0;
        for (Object object: entityList
             ) {
            MetaObject entityObject = MetaObjectUtil.forObject(object);
            sqlList.add(generateUpdateForEach(map, entity, entityObject, index));
            index++;
        }
        return String.join(";", sqlList);
    }


    /**
     * 生成批量更新里的每一句
     * @param map 上下文
     * @param entity 实体
     * @param entityObject 实体对象
     * @param index 序号
     * @return String
     */
    public String generateUpdateForEach(Map<String, Object> map, Entity entity, MetaObject entityObject, int index) {
        List<String> valueList = new ArrayList<>();

        String name;
        Object value;
        String updateDefault;
        Object id = null;

        for (EntityField fieldMap : entity.getEntityFieldMapList()
        ) {
            //主键不参与更新
            if(fieldMap.isId()) {
                id = entityObject.getValue(fieldMap.getName());
                continue;
            }

            value = entityObject.getValue(fieldMap.getName());
            name = fieldMap.getName() + "_" + index;


            // 只有本数据表字段需要更新,isForeign表示非本数据表字段,比如多表连接时，其他表字段。
            if(!fieldMap.isForeign()) {
                if (value != null) {
                    map.put(name, value);
                    valueList.add(formatUpdateItem(fieldMap, name, ""));
                } else {
                    updateDefault = fieldMap.getUpdateDefault();
                    if (!updateDefault.isEmpty()) {
                        map.put(name, updateDefault);
                        valueList.add(formatUpdateItem(fieldMap, name, updateDefault));
                    }
                }
            }
        }
        if(valueList.size()<=0) throw new RuntimeException("您没有更新任何数据");


        QueryWrapper wrapper = new QueryWrapper();
        ProviderKid.getQueryWrapper(StatementType.UPDATE, entity, wrapper);
        wrapper.setValues(valueList);
        wrapper.where(ProviderKid.getWhereId(entity, index));
        map.put(SqlUtil.getMapKey(entity.getPrimaryFieldMap().getColumn(), index), id);

        // 乐观锁处理
        ProviderKid.versionHandle(map, entity, entityObject, wrapper);
        return wrapper.getSql();
    }

}
