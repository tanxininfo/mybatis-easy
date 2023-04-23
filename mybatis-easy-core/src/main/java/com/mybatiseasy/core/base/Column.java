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
import java.util.stream.Collectors;


public class Column {
    protected List<ColumnData> columns;

    ColumnData column;

    public Column() {
        this("", "");
    }

    public Column(String table) {
        this(table, "");
    }

    public Column(String table, String tableAlias) {
        this.columns = new ArrayList<>();
        this.column = new ColumnData();
        this.column.setTableAlias(tableAlias);
        this.column.setTable(table);
    }

    public String getFullTable(){
        String fullName = this.column.getTable();
        if (TypeUtil.isNotEmpty(this.column.getTableAlias())) fullName += Sql.SPACE + "AS" + Sql.SPACE + SqlUtil.addBackquote(this.column.getTableAlias());
        return fullName;
    }

    public void addColumn(String columnName) {
        this.addColumn(columnName, "");
    }

    public void addColumn(String columnName, String columnAlias) {
        ColumnData newColumn = new ColumnData();
        newColumn.setColumn(columnName);
        if (TypeUtil.isNotEmpty(this.column.getTableAlias())) newColumn.setTableAlias(this.column.getTableAlias());
        if (TypeUtil.isNotEmpty(columnAlias)) newColumn.setColumnAlias(columnAlias);
        this.columns.add(newColumn);
        this.column = newColumn;
    }

    public void columnAlias(String columnAlias) {
        int lastIndex = this.columns.size() - 1;
        this.column.setColumnAlias(columnAlias);
        this.columns.get(lastIndex).setColumnAlias(columnAlias);
    }

    public List<String> getAllColumns() {
        return this.columns.stream().map(this::getFullColumn).collect(Collectors.toList());
    }

    public ColumnData getColumn() {
        return this.column;
    }

    public String getTableColumn() {
        if (TypeUtil.isNotEmpty(this.column.getTableAlias())) return this.column.getTableAlias() + "." + this.column;
        return this.column.getColumn();
    }

    public String getFullColumn() {
        String fullName = this.getTableColumn();
        if (TypeUtil.isNotEmpty(this.column.getColumnAlias())) fullName += Sql.SPACE + "AS" + Sql.SPACE + SqlUtil.addBackquote(this.column.getColumnAlias());
        return fullName;
    }

    public String getTableColumn(ColumnData column) {
        if (TypeUtil.isNotEmpty(column.getTableAlias())) return column.getTableAlias() + "." + column;
        return column.getColumn();
    }

    public String getFullColumn(ColumnData column) {
        String fullName = this.getTableColumn(column);
        if(TypeUtil.isNotEmpty(column.getMethod())) fullName = column.getMethod() +"("+ fullName +")";
        if (TypeUtil.isNotEmpty(column.getColumnAlias())) fullName += Sql.SPACE + "AS" + Sql.SPACE + SqlUtil.addBackquote(column.getColumnAlias());
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
    private Condition compare(Object val, String symbol, boolean apply) {
        if (!apply) return new Condition();
        String nextConditionSql = "";
        if (val instanceof Condition) nextConditionSql = ((Condition) val).getSql();
        else if (val instanceof Column) nextConditionSql = ((Column) val).getFullColumn();
        else {
            nextConditionSql = val.toString();
        }
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + nextConditionSql;
        return new Condition(sql);
    }

    private Condition compare(Object[] array, String symbol, boolean apply) {
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + SqlUtil.formatArray(array);
        return new Condition(sql);
    }

    private Condition compare(Collection<?> collection, String symbol, boolean apply) {
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + SqlUtil.formatArray(collection);
        return new Condition(sql);
    }

    private Condition compareBetween(Object val1, Object val2, boolean apply) {
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
        return compare(val, "=", true);
    }

    public Condition lt(Object val) {
        return compare(val, "<", true);
    }

    public Condition le(Object val) {
        return compare(val, "<=", true);
    }

    public Condition gt(Object val) {
        return compare(val, ">", true);
    }

    public Condition ge(Object val) {
        return compare(val, ">=", true);
    }

    public Condition ne(Object val) {
        return compare(val, "!=", true);
    }

    public Condition in(Collection<?> collection) {
        return compare(collection, "IN", true);
    }

    public Condition in(Object... array) {
        return compare(array, "IN", true);
    }

    public Condition notIn(Collection<?> collection) {
        return compare(collection, "NOT IN", true);
    }

    public Condition notIn(Object... array) {
        return compare(array, "NOT IN", true);
    }

    public Condition between(Object val1, Object val2) {
        return compareBetween(val1, val2, true);
    }

    public Condition like(Object val) {
        return compare(formatLike(val.toString().trim()), "LIKE", true);
    }

    public Condition notLike(Object val) {
        return compare(formatLike(val.toString().trim()), "NOT LIKE", true);
    }

    public Condition leftLike(Object val) {
        return compare(formatLeftLike(val.toString().trim()), "LIKE", true);
    }

    public Condition likeLeft(Object val) {
        return leftLike(val);
    }

    public Condition rightLike(Object val) {
        return compare(formatRightLike(val.toString().trim()), "LIKE", true);
    }

    public Condition likeRight(Object val) {
        return leftLike(val);
    }

    /**
     * @param apply 条件判断是否应用，当为true时应用
     * @param val   值
     * @return Condition
     */
    public Condition eq(Object val, boolean apply) {
        return compare(val, "=", apply);
    }

    public Condition lt(Object val, boolean apply) {
        return compare(val, "<", apply);
    }

    public Condition le(Object val, boolean apply) {
        return compare(val, "<=", apply);
    }

    public Condition gt(Object val, boolean apply) {
        return compare(val, ">", apply);
    }

    public Condition ge(Object val, boolean apply) {
        return compare(val, ">=", apply);
    }

    public Condition ne(Object val, boolean apply) {
        return compare(val, "!=", apply);
    }

    public Condition in(Object[] array, boolean apply) {
        return compare(array, "IN", apply);
    }

    public Condition in(Collection<?> collection, boolean apply) {
        return compare(collection, "IN", apply);
    }

    public Condition notIn(Object[] array, boolean apply) {
        return compare(array, "NOT IN", apply);
    }

    public Condition notIn(Collection<?> collection, boolean apply) {
        return compare(collection, "NOT IN", apply);
    }

    public Condition between(Object val1, Object val2, boolean apply) {
        return compareBetween(val1, val2, apply);
    }

    public Condition like(Object val, boolean apply) {return compare(formatLike(val.toString().trim()), "LIKE", apply);}

    public Condition notLike(Object val, boolean apply) { return compare(formatLike(val.toString().trim()), "NOT LIKE", apply);}

    public Condition leftLike(Object val, boolean apply) { return compare(formatLeftLike(val.toString().trim()), "LIKE", apply);}

    public Condition likeLeft(Object val, boolean apply) {
        return leftLike(val, apply);
    }

    public Condition rightLike(Object val, boolean apply) { return compare(formatRightLike(val.toString().trim()), "LIKE", apply);}

    public Condition likeRight(Object val, boolean apply) {
        return leftLike(val, apply);
    }

    protected void setMethod(String method){
        int lastIndex = this.columns.size() - 1;
        this.column.setMethod(method);
        this.columns.get(lastIndex).setMethod(method);

    }
}
