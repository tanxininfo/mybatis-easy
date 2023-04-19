package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Condition {
    private StringBuilder sql = new StringBuilder();

    public String getSql() {
        return sql.toString();
    }

    public Condition() {
    }


    public Condition(String sql) {
        this.sql = new StringBuilder(sql);
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

        return new Condition(sql.toString());
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
