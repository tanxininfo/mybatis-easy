package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Condition {
    private StringBuilder sql = new StringBuilder();
    private String table;
    private String column;

    public String getSql() {
        return sql.toString();
    }

    public Condition() {
    }

    public Condition(String table, String column) {
        this.table = table;
        this.column = column;
    }

    public Condition(String sql) {
        this.sql = new StringBuilder(sql);
    }

    /**
     * 比较运算
     *
     * @param apply  条件判断，是否应用此运算
     * @param val    值
     * @param symbol 比较符，如: =,<,<=,>,>=,!=
     * @return Condition
     */
    private Condition compare(boolean apply, Object val, String symbol) {
        if (!apply) return new Condition();
        log.info("compare={}", symbol);
        sql = new StringBuilder().append(column).append(Sql.SPACE).append(symbol).append(Sql.SPACE).append(val.toString());
        return new Condition(sql.toString());
    }

    public Condition eq(Object val) {
        return compare(true, val, "=");
    }

    public Condition lt(Object val) {
        return compare(true, val, "<");
    }

    public Condition le(Object val) {
        return compare(true, val, "<=");
    }

    public Condition gt(Object val) {
        return compare(true, val, ">");
    }

    public Condition ge(Object val) {
        return compare(true, val, ">=");
    }

    public Condition ne(Object val) {
        return compare(true, val, "!=");
    }


    /**
     * @param apply 条件判断是否应用，当为true时应用
     * @param val   值
     * @return Condition
     */
    public Condition eq(boolean apply, Object val) {
        return compare(apply, val, "=");
    }

    public Condition lt(boolean apply, Object val) {
        return compare(apply, val, "<");
    }

    public Condition le(boolean apply, Object val) {
        return compare(apply, val, "<=");
    }

    public Condition gt(boolean apply, Object val) {
        return compare(apply, val, ">");
    }

    public Condition ge(boolean apply, Object val) {
        return compare(apply, val, ">=");
    }

    public Condition ne(boolean apply, Object val) {
        return compare(apply, val, "!=");
    }

    public Condition logic(boolean apply, Condition nextCondition, String keyword) {
        if (!apply) return new Condition(sql.toString());

        boolean preEmpty = sql.isEmpty();
        boolean nextEmpty = nextCondition.getSql().isEmpty();
        if (preEmpty && nextEmpty) return new Condition();
        else {
            log.info("nextCondition.sql={}",nextCondition.sql);
            boolean need = SqlUtil.needBracket(nextCondition.sql);
            log.info("need={}",need);
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
