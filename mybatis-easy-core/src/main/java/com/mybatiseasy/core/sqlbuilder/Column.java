package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Column {
    private StringBuilder sql = new StringBuilder();
    private String table;
    private String column;

    public String getSql() {
        return sql.toString();
    }

    public Column() {
    }

    public Column(String table, String column) {
        this.table = table;
        this.column = column;
    }

    public Column(String sql) {
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
    private Column compare(boolean apply, Object val, String symbol) {
        if (!apply) return new Column();
        log.info("compare={}", symbol);
        sql = new StringBuilder().append(column).append(Sql.SPACE).append(symbol).append(Sql.SPACE).append(val.toString());
        return new Column(sql.toString());
    }

    public Column eq(Object val) {
        return compare(true, val, "=");
    }

    public Column lt(Object val) {
        return compare(true, val, "<");
    }

    public Column le(Object val) {
        return compare(true, val, "<=");
    }

    public Column gt(Object val) {
        return compare(true, val, ">");
    }

    public Column ge(Object val) {
        return compare(true, val, ">=");
    }

    public Column ne(Object val) {
        return compare(true, val, "!=");
    }


    /**
     * @param apply 条件判断是否应用，当为true时应用
     * @param val   值
     * @return Condition
     */
    public Column eq(boolean apply, Object val) {
        return compare(apply, val, "=");
    }

    public Column lt(boolean apply, Object val) {
        return compare(apply, val, "<");
    }

    public Column le(boolean apply, Object val) {
        return compare(apply, val, "<=");
    }

    public Column gt(boolean apply, Object val) {
        return compare(apply, val, ">");
    }

    public Column ge(boolean apply, Object val) {
        return compare(apply, val, ">=");
    }

    public Column ne(boolean apply, Object val) {
        return compare(apply, val, "!=");
    }

    public Column logic(boolean apply, Column nextCondition, String keyword) {
        if (!apply) return new Column(sql.toString());

        boolean preEmpty = sql.isEmpty();
        boolean nextEmpty = nextCondition.getSql().isEmpty();
        if (preEmpty && nextEmpty) return new Column();
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

        return new Column(sql.toString());
    }

    public Column and(Column nextCondition) {
        return logic(true, nextCondition, "AND");
    }

    public Column or(Column nextCondition) {
        return logic(true, nextCondition, "OR");
    }

    public Column and(boolean apply, Column nextCondition) {
        return logic(apply, nextCondition, "AND");
    }

    public Column or(boolean apply, Column nextCondition) {
        return logic(apply, nextCondition, "OR");
    }
}
