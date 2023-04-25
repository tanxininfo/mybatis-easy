package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.utils.SqlUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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


    public Condition logic(Condition nextCondition, String keyword, boolean apply) {
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
        return logic(nextCondition, "AND", true);
    }

    public Condition or(Condition nextCondition) {
        return logic(nextCondition, "OR", true);
    }

    public Condition and(Condition nextCondition, boolean apply) {
        return logic(nextCondition, "AND", apply);
    }

    public Condition or(Condition nextCondition, boolean apply) {
        return logic(nextCondition, "OR", apply);
    }
}
