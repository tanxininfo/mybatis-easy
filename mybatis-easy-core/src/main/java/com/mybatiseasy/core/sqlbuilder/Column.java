package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.utils.SqlUtil;
import com.mybatiseasy.core.utils.StringUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class Column {
    private final String table;
    private final String column;
    private String tableAlias;
    private String alias;

    public Column(String table, String column) {
        this.table = table;
        this.column = column;
    }

    private Column(String table, String column, String alias, String tableAlias) {
        this(table, column);
        this.alias = alias;
        this.tableAlias = tableAlias;
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
        String sql = column + Sql.SPACE + symbol + Sql.SPACE + val.toString();
        return new Condition(sql);
    }

    private Condition compare(boolean apply, Object[] array, String symbol) {
        if (!apply) return new Condition();
        String sql = column + Sql.SPACE + symbol + Sql.SPACE + SqlUtil.formatArray(array);
        return new Condition(sql);
    }

    private Condition compare(boolean apply, Collection<?> collection, String symbol) {
        if (!apply) return new Condition();
        String sql = column + Sql.SPACE + symbol + Sql.SPACE + SqlUtil.formatArray(collection);
        return new Condition(sql);
    }

    private Condition compareBetween(boolean apply, Object val1, Object val2) {
        if (!apply) return new Condition();
        String sql = column + Sql.SPACE + "BETWEEN" + Sql.SPACE + val1 + Sql.SPACE + "AND" + Sql.SPACE + val2;
        return new Condition(sql);
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

    public Condition in(Collection<?> collection) { return compare(true,  collection, "IN");}

    public Condition in(Object  ...array) { return compare(true, array, "IN");}

    public Condition notIn(Collection<?> collection) { return compare(true,  collection, "NOT IN");}

    public Condition notIn(Object  ...array) { return compare(true, array, "NOT IN");}

    public Condition between(Object val1, Object val2) { return compareBetween(true, val1, val2);}
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

    public Condition in(boolean apply, Object ...array) { return compare(apply, array, "IN"); }

    public Condition in(boolean apply, Collection<?> collection) {
        return compare(apply, collection, "IN");
    }

    public Condition notIn(boolean apply, Object ...array) { return compare(apply, array, "NOT IN"); }

    public Condition notIn(boolean apply, Collection<?> collection) {
        return compare(apply, collection, "NOT IN");
    }

    public Condition between(boolean apply, Object val1, Object val2) { return compareBetween(apply, val1, val2);}



    public Column as(String alias){ return new Column(table, column, alias, tableAlias); }

    public Column of(String tableAlias){ return new Column(table, column, alias, tableAlias); }

    public String getColumn(){
        return this.column;
    }

    public String getFullColumn(){
        String fullName = "";
        if(TypeUtil.isNotEmpty(this.tableAlias)) fullName = this.tableAlias + ".";
        fullName += this.column;
        if(TypeUtil.isNotEmpty(this.alias)) fullName += Sql.SPACE + "AS" + Sql.SPACE + this.alias;
        return fullName;
    }

}
