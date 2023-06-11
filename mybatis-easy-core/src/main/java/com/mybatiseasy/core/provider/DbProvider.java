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

import com.mybatiseasy.core.base.Table;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.enums.StatementType;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;
import java.util.Map;


@Slf4j
public class DbProvider {
    private DbProvider() {
    }

    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String insert(Map<String, Object> map, ProviderContext context) {
        Class<?> clazz = (Class<?>) map.get(MethodParam.ENTITY_CLASS);
        EntityMap entityMap = EntityMapKids.getEntityMap(clazz.getName());
        assert entityMap != null;

        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertParts(map, entityMap, MethodParam.RECORD);

        QueryWrapper wrapper = new QueryWrapper();

        wrapper.insertInto(entityMap.getName());
        wrapper.columns(builder.getInsertSymbolList().toArray());
        wrapper.valuesList(builder.getInsertValuesList());

        return wrapper.getSql();
    }

    /**
     * 插入一组记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String insertBatch(Map<String, Object> map, ProviderContext context) {
        Class<?> clazz = (Class<?>) map.get(MethodParam.ENTITY_CLASS);
        EntityMap entityMap = EntityMapKids.getEntityMap(clazz.getName());
        assert entityMap != null;
        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertBatchParts(map, entityMap);

        QueryWrapper wrapper = new QueryWrapper();

        wrapper.insertInto(entityMap.getName());
        wrapper.columns(builder.getInsertSymbolList().toArray());
        wrapper.valuesList(builder.getInsertValuesList());

        return wrapper.getSql();
    }


    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String updateByWrapper(Map<String, Object> map, ProviderContext context) {
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);

        List<Table> tableList = wrapper.getTableList();
        assert tableList.size()>0;

        EntityMap entityMap = EntityMapKids.getEntityMap(tableList.get(0).getColumn().getEntityName());
        assert entityMap != null;

        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());


        DbProviderKid.getQueryWrapper(StatementType.UPDATE, wrapper);

        SqlBuilder builder = new SqlBuilder();
        builder.generateUpdateParts(map, entityMap);
        wrapper.setValues(builder.getUpdateValueList());

        MetaObject entityObj = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));

        // 乐观锁处理
        ProviderKid.versionHandle(map, entityMap, entityObj, wrapper);

        return wrapper.getSql();
    }

    /**
     * 根据包装类删除多条记录
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String deleteByWrapper(Map<String, Object> map, ProviderContext context) {
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        //逻辑删除处理
        List<Table> tableList = wrapper.getTableList();
        assert tableList.size() > 0;

        EntityMap entityMap = EntityMapKids.getEntityMap(tableList.get(0).getColumn().getEntityName());
        assert entityMap != null;
        ProviderKid.logicDeleteHandle(wrapper, entityMap);


        if (!wrapper.hasWhere() && (map.get("force") == null)) throw new RuntimeException("删除条件不得为空");

        return wrapper.getSql();
    }



    /**
     * 根据包装类查询一条实体
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String getByWrapper(Map<String, Object> map, ProviderContext context) {
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        DbProviderKid.getQueryWrapper(StatementType.SELECT, wrapper);

        return wrapper.getSql();
    }

    /**
     * 根据包装类查询实体列表
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String listByWrapper(Map<String, Object> map, ProviderContext context) {
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());
        DbProviderKid.getQueryWrapper(StatementType.SELECT, wrapper);
        return wrapper.getSql();
    }

    /**
     * 根据组合条件统计记录数
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String countByWrapper(Map<String, Object> map, ProviderContext context) {
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        DbProviderKid.getQueryWrapper(StatementType.COUNT, wrapper);

        return wrapper.getSql();
    }

    /**
     * 根据包装类分页查询
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String queryEasy(Map<String, Object> map, ProviderContext context) {
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

       DbProviderKid.getQueryWrapper(StatementType.SELECT, wrapper);

        return wrapper.getSqlPaginate();
    }
}
