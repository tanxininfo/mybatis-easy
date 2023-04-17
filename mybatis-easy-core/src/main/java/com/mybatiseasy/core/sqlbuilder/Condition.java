package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Condition {
    private StringBuilder sql = new StringBuilder();
    private String table;
    private String column;

    public String getSql(){
        return sql.toString();
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
     * @param val 值
     * @param symbol 比较符，如: =,<,<=,>,>=,!=
     * @return Condition
     */
    private Condition compare(Object val, String symbol) {
        sql = new StringBuilder().append(column).append(Sql.SPACE).append(symbol).append(Sql.SPACE).append(val.toString());
        return new Condition(sql.toString());
    }

    public Condition eq(Object val) {
        return compare(val, "=");
    }

    public Condition lt(Object val) {
        return compare(val, "<");
    }

    public Condition le(Object val) {
        return compare(val, "<=");
    }

    public Condition gt(Object val) {
        return compare(val, ">");
    }

    public Condition ge(Object val) {
        return compare(val, ">=");
    }

    public Condition ne(Object val) {
        return compare(val, "!=");
    }

    public Condition logic(Condition nextCondition, String keyword) {
        sql.append(Sql.SPACE).append(keyword).append(Sql.SPACE).append(Sql.LEFT_BRACKET).append(nextCondition.sql).append(Sql.RIGHT_BRACKET);
        return  new Condition(sql.toString());
    }

    public Condition and(Condition nextCondition) {
       return logic(nextCondition, "AND");
    }

    public Condition or(Condition nextCondition) {
        return logic(nextCondition, "OR");
    }
}
