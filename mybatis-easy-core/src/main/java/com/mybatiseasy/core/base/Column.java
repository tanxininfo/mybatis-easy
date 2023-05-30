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

package com.mybatiseasy.core.base;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.utils.SqlUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class Column {
    protected List<ColumnData> columns;
    protected Map<String, Object> parameterMap;

    ColumnData column;

    public Column() {
        this("", "");
    }

    public Column(String entity, String table) {
        this(entity, table, "");
    }

    public Column(String entity, String table, String tableAlias) {
        this.columns = new ArrayList<>();
        this.column = new ColumnData();
        this.parameterMap = new HashMap<>();
        this.column.setEntityName(entity);
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
        if(this.column!= null) {
            newColumn.setEntityName(this.column.getEntityName());
            newColumn.setTable(this.column.getTable());
            if (TypeUtil.isNotEmpty(this.column.getTableAlias())) newColumn.setTableAlias(this.column.getTableAlias());
        }

        if (TypeUtil.isNotEmpty(columnAlias)) newColumn.setColumnAlias(columnAlias);
        if(this.columns.stream().noneMatch(item ->
                item.getTable().equals(newColumn.getTable())
                        && item.getTableAlias().equals(newColumn.getTableAlias())
                        && item.getColumn().equals(newColumn.getColumn()))) {
            this.columns.add(newColumn);
        }
        this.column = newColumn;
    }

    /**
     * 当出现在非 select语句下时，要去除新添加的column,因为此时并不是为了表示select字段。
     * 例如：在where语句中，新添加了column,但是该column会出现在select 语句中，所以要删除。
     */
    public void removeLastColumn() {
        if (this.columns.size() > 0)
            this.columns.remove(this.columns.size() - 1);
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
        if (TypeUtil.isNotEmpty(this.column.getTableAlias())) return this.column.getTableAlias() + "." + this.column.getColumn();
        return this.column.getColumn();
    }

    public String getFullColumn() {
        String fullName = this.getTableColumn();
        if (TypeUtil.isNotEmpty(this.column.getColumnAlias())) fullName += Sql.SPACE + "AS" + Sql.SPACE + SqlUtil.addBackquote(this.column.getColumnAlias());
        return fullName;
    }

    public String getTableColumn(ColumnData column) {
        if (TypeUtil.isNotEmpty(column.getTableAlias())) return column.getTableAlias() + "." + column.getColumn();
        return column.getColumn();
    }

    public String getFullColumn(ColumnData column) {
        String fullName = this.getTableColumn(column);
        if(TypeUtil.isNotEmpty(column.getMethod())) fullName = column.getMethod() +"("+ fullName +")";
        if (TypeUtil.isNotEmpty(column.getColumnAlias())) fullName += Sql.SPACE + "AS" + Sql.SPACE + SqlUtil.addBackquote(column.getColumnAlias());
        return fullName;
    }

    /**
     * 创建值替位符，如:#{value}
     * @return String
     */
    private String getValueTag(Object value){
        String key = SqlUtil.getMapKey(this.column.getColumn());
        this.parameterMap.put(key, value);
        return "#{"+ key  +"}";
    }

    /**
     * 创建值替位符，如:#{value}
     * 注意new Integer[]{1,2,3} 而不能是new int[]{1, 2, 3}
     * @return String
     */
    private String getValueTagArray(Object[] array){
        String key = SqlUtil.getMapKey(this.column.getColumn());
        this.parameterMap.put(key, array);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("#{").append(key).append("[");
            sb.append(i);
            sb.append("]}");
        }

        return sb.toString();
    }

    /**
     * 创建值替位符，如:#{value}
     * @return String
     */
    private String getValueTagCollection(Collection<?> collection){
        String key = SqlUtil.getMapKey(this.column.getColumn());
        this.parameterMap.put(key, collection);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < collection.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("#{").append(key).append("[");
            sb.append(i);
            sb.append("]}");
        }

        return sb.toString();
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
        this.removeLastColumn();
        String nextConditionSql = "";
        if (val instanceof Condition) nextConditionSql = ((Condition) val).getSql();
        else if (val instanceof Column) {
            ((Column) val).removeLastColumn();
            nextConditionSql = ((Column) val).getFullColumn();
        }
        else {
            nextConditionSql = getValueTag(val.toString());
        }

        //不应用条件判断时，直接返回空的Condition
        if (!apply) return new Condition();

        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + nextConditionSql;
        return new Condition(sql, this.parameterMap);
    }

    private Condition compare(Object[] array, String symbol, boolean apply) {
        this.removeLastColumn();
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + "("+ getValueTagArray(array) +")";
        return new Condition(sql, this.parameterMap);
    }

    private Condition compare(Collection<?> collection, String symbol, boolean apply) {
        this.removeLastColumn();
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + symbol + Sql.SPACE + "("+ getValueTagCollection(collection) +")";
        return new Condition(sql, this.parameterMap);
    }

    private Condition compareBetween(Object val1, Object val2, boolean apply) {
        this.removeLastColumn();
        if (!apply) return new Condition();
        String sql = this.getTableColumn() + Sql.SPACE + "BETWEEN" + Sql.SPACE + getValueTag(val1) + Sql.SPACE + "AND" + Sql.SPACE + getValueTag(val2);
        return new Condition(sql, this.parameterMap);
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
