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
import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.SqlUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProviderKid {


    public static QueryWrapper getQueryWrapper(StatementType statementType, EntityMap entityMap){
        return getQueryWrapper(statementType, entityMap, new QueryWrapper());
    }

    public static QueryWrapper getQueryWrapper(StatementType statementType, EntityMap entityMap, QueryWrapper wrapper){

        Table table = new Table(entityMap.getFullName(), entityMap.getName());

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

    public static void putIdValueToMap(Map<String, Object> map, EntityMap entityMap, MetaObject entityObj){
        String idKey = entityMap.getPrimaryFieldMap().getName();
        Object idValue = (entityObj!=null ? entityObj.getValue(idKey): map.get(MethodParam.PRIMARY_KEY));
        Assert.notNull(idValue, "请指定实体主键值");
        map.put(idKey, idValue);
    }

    /**
     * 取得 where id={#id} 的 id={#id}部份
     * @param entityMap 实体映射对象
     * @return String `id`={#id}
     */
    public static String getWhereId(EntityMap entityMap){

        return entityMap.getPrimaryFieldMap().getColumn() + "=" + Sql.SPACE +
                "#{" + entityMap.getPrimaryFieldMap().getName() +"}";
    }


    public static void versionHandle(Map<String, Object> map, EntityMap entityMap, MetaObject entityObj, QueryWrapper queryWrapper){
        EntityFieldMap version = entityMap.getVersionFieldMap();
        if(version == null) return;

        Object value = entityObj.getValue(version.getName());
        String key = SqlUtil.getMapKey(version.getColumn());
        String valueTag = SqlUtil.getValueTag(key);

        queryWrapper.where(SqlUtil.addBackquote(version.getColumn()) +" = "+ valueTag);
        queryWrapper.addParameter(key, value);
        map.put(key, value);
    }

    public static void logicDeleteHandle(QueryWrapper wrapper, EntityMap entityMap){
        EntityFieldMap logicDeleteFieldMap =  entityMap.getLogicDeleteFieldMap();
        if(logicDeleteFieldMap == null) {
            getQueryWrapper(StatementType.DELETE, entityMap, wrapper);
        }else{
            getQueryWrapper(StatementType.UPDATE, entityMap, wrapper);
            List<String> values = new ArrayList<>();
            values.add(logicDeleteFieldMap.getColumn()+" = " + logicDeleteFieldMap.getLogicDeleteValue());
            wrapper.setValues(values);
        }
    }
}
