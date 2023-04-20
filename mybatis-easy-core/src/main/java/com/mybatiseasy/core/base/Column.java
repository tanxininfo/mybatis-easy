package com.mybatiseasy.core.base;

import cn.hutool.core.annotation.Alias;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.utils.SqlUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import org.springframework.core.annotation.AliasFor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Column {
    protected List<String> columns;
    protected String tableAlias;

    protected String table;

    protected String column;

    protected String columnAlias;

    public Column() {
        this.columns = new ArrayList<>();
    }

    public Column(String table) {
        this.columns = new ArrayList<>();
        this.table = table;
    }

    public Column(String table, String tableAlias) {
        this.columns = new ArrayList<>();
        this.tableAlias = tableAlias;
        this.table = table;
    }

    public String getFullTable(){
        String fullName = this.table;
        if (TypeUtil.isNotEmpty(this.tableAlias)) fullName += Sql.SPACE + "AS" + Sql.SPACE + this.tableAlias;
        return fullName;
    }

    public void addColumn(String newColumn) {
        String fullColumn = newColumn;
        if (TypeUtil.isEmpty(this.columns)) this.columns = new ArrayList<>();
        if (TypeUtil.isNotEmpty(this.tableAlias)) fullColumn = this.tableAlias + "." + newColumn;
        this.columns.add(fullColumn);
        this.column = newColumn;
    }

    public void addColumn(String newColumn, String columnAlias) {
        String fullColumn = newColumn;
        if (TypeUtil.isEmpty(this.columns)) this.columns = new ArrayList<>();
        if (TypeUtil.isNotEmpty(this.tableAlias)) fullColumn = this.tableAlias + "." + newColumn;
        if (TypeUtil.isNotEmpty(columnAlias)) fullColumn += Sql.SPACE + "AS" + Sql.SPACE + columnAlias;
        this.columns.add(fullColumn);
        this.column = newColumn;
        this.columnAlias = columnAlias;
    }

    public void columnAlias(String columnAlias) {
        int lastIndex = this.columns.size() - 1;
        this.columnAlias = columnAlias;
        this.columns.set(lastIndex, this.columns.get(lastIndex) + Sql.SPACE + "AS" + Sql.SPACE + columnAlias);
    }

    public List<String> getAllColumns() {
        return this.columns;
    }

    public String getColumn() {
        return this.column;
    }

    public String getTableColumn() {
        if (TypeUtil.isNotEmpty(this.tableAlias)) return this.tableAlias + "." + this.column;
        return this.column;
    }

    public String getFullColumn() {
        String fullName = this.getTableColumn();
        if (TypeUtil.isNotEmpty(this.columnAlias)) fullName += Sql.SPACE + "AS" + Sql.SPACE + this.columnAlias;
        return fullName;
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
        String nextConditionSql = "";
        if (val instanceof Condition) nextConditionSql = ((Condition) val).getSql();
        else if (val instanceof Column) nextConditionSql = ((Column) val).getFullColumn();
        else nextConditionSql = val.toString();
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + nextConditionSql;
        return new Condition(sql);
    }

    private Condition compare(boolean apply, Object[] array, String symbol) {
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + SqlUtil.formatArray(array);
        return new Condition(sql);
    }

    private Condition compare(boolean apply, Collection<?> collection, String symbol) {
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + SqlUtil.formatArray(collection);
        return new Condition(sql);
    }

    private Condition compareBetween(boolean apply, Object val1, Object val2) {
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + "BETWEEN" + Sql.SPACE + val1 + Sql.SPACE + "AND" + Sql.SPACE + val2;
        return new Condition(sql);
    }

    private String formatLike(String value) {
        if (value.startsWith("%") || value.endsWith("%")) return SqlUtil.addSingQuote(value);
        return SqlUtil.addSingQuote("%" + value + "%");
    }

    private String formatLeftLike(String value) {
        if (value.startsWith("%")) return SqlUtil.addSingQuote(value);
        return SqlUtil.addSingQuote("%" + value);
    }

    private String formatRightLike(String value) {
        if (value.endsWith("%")) return SqlUtil.addSingQuote(value);
        return SqlUtil.addSingQuote(value + "%");
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

    public Condition in(Collection<?> collection) {
        return compare(true, collection, "IN");
    }

    public Condition in(Object... array) {
        return compare(true, array, "IN");
    }

    public Condition notIn(Collection<?> collection) {
        return compare(true, collection, "NOT IN");
    }

    public Condition notIn(Object... array) {
        return compare(true, array, "NOT IN");
    }

    public Condition between(Object val1, Object val2) {
        return compareBetween(true, val1, val2);
    }

    public Condition like(Object val) {
        return compare(true, formatLike(val.toString().trim()), "LIKE");
    }

    public Condition notLike(Object val) {
        return compare(true, formatLike(val.toString().trim()), "NOT LIKE");
    }

    public Condition leftLike(Object val) {
        return compare(true, formatLeftLike(val.toString().trim()), "LIKE");
    }

    public Condition likeLeft(Object val) {
        return leftLike(val);
    }

    public Condition rightLike(Object val) {
        return compare(true, formatRightLike(val.toString().trim()), "LIKE");
    }

    public Condition likeRight(Object val) {
        return leftLike(val);
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

    public Condition in(boolean apply, Object... array) {
        return compare(apply, array, "IN");
    }

    public Condition in(boolean apply, Collection<?> collection) {
        return compare(apply, collection, "IN");
    }

    public Condition notIn(boolean apply, Object... array) {
        return compare(apply, array, "NOT IN");
    }

    public Condition notIn(boolean apply, Collection<?> collection) {
        return compare(apply, collection, "NOT IN");
    }

    public Condition between(boolean apply, Object val1, Object val2) {
        return compareBetween(apply, val1, val2);
    }

    public Condition like(boolean apply, Object val) {return compare(apply, formatLike(val.toString().trim()), "LIKE");}

    public Condition notLike(boolean apply, Object val) { return compare(apply, formatLike(val.toString().trim()), "NOT LIKE");}

    public Condition leftLike(boolean apply, Object val) { return compare(apply, formatLeftLike(val.toString().trim()), "LIKE");}

    public Condition likeLeft(boolean apply, Object val) {
        return leftLike(apply, val);
    }

    public Condition rightLike(boolean apply, Object val) { return compare(apply, formatRightLike(val.toString().trim()), "LIKE");}

    public Condition likeRight(boolean apply, Object val) {
        return leftLike(apply, val);
    }

}
