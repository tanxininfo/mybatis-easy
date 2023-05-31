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

import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.enums.StatementType;
import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SqlProvider {
    private SqlProvider() {
    }

    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String insert(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);

        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertParts(map, entityMap);

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
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertBatchParts(map, entityMap);

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.insertInto(entityMap.getName());
        wrapper.columns(builder.getInsertSymbolList().toArray());
        wrapper.valuesList(builder.getInsertValuesList());

        return wrapper.getSql();
    }

    /**
     * 修改一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String updateById(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Assert.notNull(entityMap.getPrimaryFieldMap(), "实体类未标注TableId");
        MetaObject entityObj = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));
        ProviderKid.putIdValueToMap(map, entityMap, entityObj);

        SqlBuilder builder = new SqlBuilder();
        builder.generateUpdateParts(map, entityMap);

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.UPDATE, entityMap);
        wrapper.setValues(builder.getUpdateValueList());

        // 乐观锁处理
        ProviderKid.versionHandle(map, entityMap, entityObj, wrapper);
        wrapper.where(ProviderKid.getWhereId(entityMap));

        return wrapper.getSql();
    }

    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String updateByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.UPDATE, entityMap);

        SqlBuilder builder = new SqlBuilder();
        builder.generateUpdateParts(map, entityMap);
        wrapper.setValues(builder.getUpdateValueList());

        wrapper.where(condition);

        MetaObject entityObj = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));

        // 乐观锁处理
        ProviderKid.versionHandle(map, entityMap, entityObj, wrapper);
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
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        ProviderKid.getQueryWrapper(StatementType.UPDATE, entityMap, wrapper);

        SqlBuilder builder = new SqlBuilder();
        builder.generateUpdateParts(map, entityMap);
        wrapper.setValues(builder.getUpdateValueList());

        MetaObject entityObj = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));

        // 乐观锁处理
        ProviderKid.versionHandle(map, entityMap, entityObj, wrapper);

        return wrapper.getSql();
    }


    /**
     * 更新一组记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String updateByIdBatch(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);

        SqlBuilder builder = new SqlBuilder();
        return builder.generateUpdateByIdBatchSql(map, entityMap);
    }

    /**
     * 删除一条记录
     *
     * @param map 条件
     * @param context  上下文
     * @return
     */
    public static String deleteById(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Assert.notNull(entityMap.getPrimaryFieldMap(), "实体类未标注TableId");
        ProviderKid.putIdValueToMap(map, entityMap, null);

        QueryWrapper wrapper = new QueryWrapper();
        //逻辑删除处理
        ProviderKid.logicDeleteHandle(wrapper, entityMap);

        wrapper.where(ProviderKid.getWhereId(entityMap));

        return wrapper.getSql();
    }

    /**
     * 删除多条记录, 根据Condition
     *
     * @param map 参数
     * @param context 上下文
     * @return 影响行数
     */
    public static String deleteByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = new QueryWrapper();
        //逻辑删除处理
        ProviderKid.logicDeleteHandle(wrapper, entityMap);

        wrapper.where(condition);

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
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        //逻辑删除处理
        ProviderKid.logicDeleteHandle(wrapper, entityMap);

        Assert.isTrue(wrapper.hasWhere() || (map.get("force") != null), "删除条件不得为空");

        return wrapper.getSql();
    }

    /**
     * 根据Id查询一个实体
     *
     * @param map 参数map
     * @param context 上下文
     * @return String
     */
    public static String getById(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Assert.notNull(entityMap.getPrimaryFieldMap(), "实体类未标注TableId");
        ProviderKid.putIdValueToMap(map, entityMap, null);

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entityMap);
        wrapper.where(ProviderKid.getWhereId(entityMap));

        return wrapper.getSql();
    }

    /**
     * 根据组合条件查询一个实体
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String getByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entityMap);
        wrapper.where(condition);

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
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        ProviderKid.getQueryWrapper(StatementType.SELECT, entityMap, wrapper);

        return wrapper.getSql();
    }

    /**
     * 根据组合条件查询实体列表
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String listByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entityMap);
        wrapper.where(condition);

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
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        ProviderKid.getQueryWrapper(StatementType.SELECT, entityMap, wrapper);
        return wrapper.getSql();
    }

    /**
     * 根据组合条件统计记录数
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String countByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entityMap);
        wrapper.where(condition);

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
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        ProviderKid.getQueryWrapper(StatementType.COUNT, entityMap, wrapper);

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
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

       ProviderKid.getQueryWrapper(StatementType.SELECT, entityMap, wrapper);

        return wrapper.getSqlPaginate();
    }
}
