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

package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.utils.SqlUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

public class Condition {
    private StringBuilder sql = new StringBuilder();
    private Map<String, Object> parameterMap = new HashMap<>();

    public String getSql() {
        return sql.toString();
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public Condition() {
    }


    public Condition(String sql) {
        this.sql = new StringBuilder(sql);
    }

    public Condition(String sql, Map<String, Object> parameterMap) {
        this.sql = new StringBuilder(sql);
        this.parameterMap = parameterMap;
    }


    public Condition logic(boolean apply, Condition nextCondition, String keyword) {
        if (!apply) return new Condition(sql.toString());

        boolean preEmpty = sql.isEmpty();
        boolean nextEmpty = nextCondition.getSql().isEmpty();
        if (preEmpty && nextEmpty) return new Condition();
        else {
            boolean need = SqlUtil.needBracket(nextCondition.sql);
            if (preEmpty) {
                if (need)
                    sql.append(Sql.LEFT_BRACKET).append(nextCondition.sql).append(Sql.RIGHT_BRACKET);
                else
                    sql.append(nextCondition.sql);
            } else if (!nextEmpty) {
                if (need)
                    sql.append(Sql.SPACE).append(keyword).append(Sql.SPACE).append(Sql.LEFT_BRACKET).append(nextCondition.sql).append(Sql.RIGHT_BRACKET);
                else
                    sql.append(Sql.SPACE).append(keyword).append(Sql.SPACE).append(nextCondition.sql);
            }
        }
        this.parameterMap.putAll(nextCondition.getParameterMap());
        return new Condition(sql.toString(), this.parameterMap);
    }

    public Condition and(Condition nextCondition) {
        return logic(true, nextCondition, "AND");
    }

    public Condition or(Condition nextCondition) {
        return logic(true, nextCondition, "OR");
    }

    public Condition and(boolean apply, Condition nextCondition) {
        return logic(apply, nextCondition, "AND");
    }

    public Condition or(boolean apply, Condition nextCondition) {
        return logic(apply, nextCondition, "OR");
    }
}
