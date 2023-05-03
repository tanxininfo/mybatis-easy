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
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.Assert;

import java.util.Map;

public class ProviderKid {


    public static QueryWrapper getQueryWrapper(StatementType statementType, EntityMap entityMap){
        return getQueryWrapper(statementType, entityMap, new QueryWrapper());
    }

    public static QueryWrapper getQueryWrapper(StatementType statementType, EntityMap entityMap, QueryWrapper wrapper){
        String entityName = entityMap.getName();
        switch (statementType) {
            case SELECT -> {
                if (!wrapper.hasSelect()) wrapper.select("*");
                if (!wrapper.hasTable()) wrapper.from(new Column(entityName));
            }
            case COUNT -> {
                if (!wrapper.hasSelect()) wrapper.select("count(*)");
                if (!wrapper.hasTable()) wrapper.from(new Column(entityName));
            }
            case DELETE ->{
                if (!wrapper.hasTable()) wrapper.deleteFrom(new Column(entityName));
            }
            case INSERT -> {
                if (!wrapper.hasTable()) wrapper.insertInto(new Column(entityName));
            }
            case UPDATE -> {
                if (!wrapper.hasTable()) wrapper.update(new Column(entityName));
            }
        }
        return wrapper;
    }

    public static void putIdValueToMap(Map<String, Object> map, EntityMap entityMap, MetaObject entityObj){
        String idKey = entityMap.getPrimary().getName();
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

        return entityMap.getPrimary().getColumn() + "=" + Sql.SPACE +
                "#{" + entityMap.getPrimary().getName() +"}";
    }
}
