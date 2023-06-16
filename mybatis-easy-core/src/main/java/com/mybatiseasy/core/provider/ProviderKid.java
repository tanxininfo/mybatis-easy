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
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.enums.StatementType;
import com.mybatiseasy.core.session.EntityField;
import com.mybatiseasy.core.session.Entity;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.SqlUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProviderKid {


    public static QueryWrapper getQueryWrapper(StatementType statementType, Entity entity){
        return getQueryWrapper(statementType, entity, new QueryWrapper());
    }

    public static QueryWrapper getQueryWrapper(StatementType statementType, Entity entity, QueryWrapper wrapper){

        Table table = new Table(entity.getFullName(), entity.getName(), "");

        switch (statementType) {
            case SELECT -> {
                if (!wrapper.hasSelect()) wrapper.select("*");
                if (!wrapper.hasTable()) wrapper.from(table);
            }
            case COUNT -> {
                if (!wrapper.hasSelect()) wrapper.select("count(*)");
                if (!wrapper.hasTable()) wrapper.from(table);
            }
            case DELETE ->{
                if (!wrapper.hasTable()) wrapper.deleteFrom(table);
            }
            case INSERT -> {
                if (!wrapper.hasTable()) wrapper.insertInto(table);
            }
            case UPDATE -> {
                if (!wrapper.hasTable()) wrapper.update(table);
            }
        }
        return wrapper;
    }

    public static void putIdValueToMap(Map<String, Object> map, Entity entity, MetaObject entityObj){
        String idKey = entity.getPrimaryFieldMap().getName();
        Object idValue = (entityObj!=null ? entityObj.getValue(idKey): map.get(MethodParam.PRIMARY_KEY));
        if(idValue == null) throw new RuntimeException("请指定实体主键值");
        map.put(idKey, idValue);
    }

    /**
     * 取得 where id={#id} 的 id={#id}部份
     * @param entity 实体映射对象
     * @return String `id`={#id}
     */
    public static String getWhereId(Entity entity){

        return entity.getPrimaryFieldMap().getColumn() + "=" + Sql.SPACE +
                "#{" + entity.getPrimaryFieldMap().getName() +"}";
    }

    /**
     * 取得 where id={#id} 的 id={#id_1}部份
     * @param entity 实体映射对象
     * @return String `id`={#id_1}
     */
    public static String getWhereId(Entity entity, int index){
        return entity.getPrimaryFieldMap().getColumn() + "=" + Sql.SPACE +
                SqlUtil.getValueTag(SqlUtil.getMapKey(entity.getPrimaryFieldMap().getColumn(), index));
    }

    public static void versionHandle(Map<String, Object> map, Entity entity, MetaObject entityObj, QueryWrapper queryWrapper){
        EntityField version = entity.getVersionFieldMap();
        if(version == null) return;

        Object value = entityObj.getValue(version.getName());
        String key = SqlUtil.getMapKey(version.getColumn());
        String valueTag = SqlUtil.getValueTag(key);

        queryWrapper.where(SqlUtil.addBackquote(version.getColumn()) +" = "+ valueTag);
        queryWrapper.addParameter(key, value);
        map.put(key, value);
    }

    public static void logicDeleteHandle(QueryWrapper wrapper, Entity entity){
        EntityField logicDeleteFieldMap =  entity.getLogicDeleteFieldMap();
        if(logicDeleteFieldMap == null) {
            getQueryWrapper(StatementType.DELETE, entity, wrapper);
        }else{
            getQueryWrapper(StatementType.UPDATE, entity, wrapper);
            List<String> values = new ArrayList<>();
            values.add(logicDeleteFieldMap.getColumn()+" = " + logicDeleteFieldMap.getLogicDeleteValue());
            wrapper.setValues(values);
        }
    }
}
