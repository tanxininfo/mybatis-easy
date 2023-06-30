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
import com.mybatiseasy.core.session.Entity;
import com.mybatiseasy.core.session.EntityKids;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Map;

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
        Entity entity = EntityKids.getEntityMapByContext(context);

        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertParts(map, entity);

        QueryWrapper wrapper = new QueryWrapper();

        wrapper.insertInto(entity.getName());
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
        Entity entity = EntityKids.getEntityMapByContext(context);
        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertBatchParts(map, entity);

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.insertInto(entity.getName());
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
        Entity entity = EntityKids.getEntityMapByContext(context);
        if(entity.getPrimaryFieldMap() == null) throw new RuntimeException("实体类未标注TableId");

        MetaObject entityObj = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));
        ProviderKid.putIdValueToMap(map, entity, entityObj);

        SqlBuilder builder = new SqlBuilder();
        builder.generateUpdateParts(map, entity);

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.UPDATE, entity);
        wrapper.setValues(builder.getUpdateValueList());

        // 乐观锁处理
        ProviderKid.versionHandle(map, entity, entityObj, wrapper);
        wrapper.where(ProviderKid.getWhereId(entity));

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
        Entity entity = EntityKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.UPDATE, entity);

        SqlBuilder builder = new SqlBuilder();
        builder.generateUpdateParts(map, entity);
        wrapper.setValues(builder.getUpdateValueList());

        wrapper.where(condition);

        MetaObject entityObj = MetaObjectUtil.forObject(map.get(MethodParam.ENTITY));

        // 乐观锁处理
        ProviderKid.versionHandle(map, entity, entityObj, wrapper);
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
        Entity entity = EntityKids.getEntityMapByContext(context);

        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        ProviderKid.getQueryWrapper(StatementType.UPDATE, entity, wrapper);

        SqlBuilder builder = new SqlBuilder();
        builder.generateUpdateParts(map, entity, MethodParam.RECORD);
        wrapper.setValues(builder.getUpdateValueList());

        MetaObject entityObj = MetaObjectUtil.forObject(map.get(MethodParam.RECORD));

        // 乐观锁处理
        ProviderKid.versionHandle(map, entity, entityObj, wrapper);

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
        Entity entity = EntityKids.getEntityMapByContext(context);

        SqlBuilder builder = new SqlBuilder();
        return builder.generateUpdateByIdBatchSql(map, entity);
    }

    /**
     * 删除一条记录
     *
     * @param map 条件
     * @param context  上下文
     * @return
     */
    public static String deleteById(Map<String, Object> map, ProviderContext context) {
        Entity entity = EntityKids.getEntityMapByContext(context);
        if(entity.getPrimaryFieldMap() == null) throw new RuntimeException("实体类未标注TableId");

        ProviderKid.putIdValueToMap(map, entity, null);

        QueryWrapper wrapper = new QueryWrapper();
        //逻辑删除处理
        ProviderKid.logicDeleteHandle(wrapper, entity);

        wrapper.where(ProviderKid.getWhereId(entity));

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
        Entity entity = EntityKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = new QueryWrapper();
        //逻辑删除处理
        ProviderKid.logicDeleteHandle(wrapper, entity);

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
        Entity entity = EntityKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        //逻辑删除处理
        ProviderKid.logicDeleteHandle(wrapper, entity);

        if(!wrapper.hasWhere() && (map.get("force") == null)) throw new RuntimeException("删除条件不得为空");


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
        Entity entity = EntityKids.getEntityMapByContext(context);

        if(entity.getPrimaryFieldMap() == null) throw new RuntimeException("实体类未标注TableId");

        ProviderKid.putIdValueToMap(map, entity, null);

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entity);
        wrapper.where(ProviderKid.getWhereId(entity));

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
        Entity entity = EntityKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entity);
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
        Entity entity = EntityKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        ProviderKid.getQueryWrapper(StatementType.SELECT, entity, wrapper);

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
        Entity entity = EntityKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entity);
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
        Entity entity = EntityKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        ProviderKid.getQueryWrapper(StatementType.SELECT, entity, wrapper);
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
        Entity entity = EntityKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = ProviderKid.getQueryWrapper(StatementType.SELECT, entity);
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
        Entity entity = EntityKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

        ProviderKid.getQueryWrapper(StatementType.COUNT, entity, wrapper);

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
        Entity entity = EntityKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getParameterMap());

       ProviderKid.getQueryWrapper(StatementType.SELECT, entity, wrapper);

        return wrapper.getSqlPaginate();
    }
}
