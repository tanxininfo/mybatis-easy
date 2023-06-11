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

import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.base.ColumnData;
import com.mybatiseasy.core.base.Table;
import com.mybatiseasy.core.config.GlobalConfig;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.enums.StatementType;
import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.utils.SqlUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Slf4j
public class QueryWrapper implements Serializable {

    private boolean ignoreTenantId = false;

    public QueryWrapper(){
    }

    public static QueryWrapper create(){
        return new QueryWrapper();
    }

    private final SQLStatement sqlStatement = new SQLStatement();

    public List<Table> getTableList() {
        return this.sqlStatement.tableList;
    }

    public QueryWrapper ignoreTenant(){
        this.ignoreTenantId = true;
        return this;
    }

    public QueryWrapper orderBy(Column column, boolean isDesc){
        column.removeLastColumn();
        sqlStatement.orderBy.add(column.getTableColumn()+ Sql.SPACE + (isDesc? "DESC":"ASC"));
        return this;
    }

    public QueryWrapper orderBy(String columnName, boolean isDesc){
        sqlStatement.orderBy.add(columnName+ Sql.SPACE + (isDesc? "DESC":"ASC"));
        return this;
    }

    private String formatJoin(Table table, Condition condition) {
        sqlStatement.tableList.add(table);
        Condition logicDeleteCondition = getLogicDeleteCondition(table);
        if (logicDeleteCondition != null) condition.and(logicDeleteCondition);

        Condition tenantCondition = getTenantCondition(table);
        if (tenantCondition != null) condition.and(tenantCondition);

        return table.getFullTable() + Sql.SPACE + "ON" + Sql.SPACE + condition.getSql();
    }

    /**
     * 取得tenantId语句
     * @param table Table
     * @return Condition
     */
    private Condition getTenantCondition(Table table){
        if(ignoreTenantId) return null;
        String entityName = table.getColumn().getEntityName();
        EntityMap entityMap = EntityMapKids.getEntityMap(entityName);
        assert entityMap != null;
        EntityFieldMap tenantIdField = entityMap.getTenantIdFieldMap();
        if(tenantIdField != null){
            ColumnData columnData = table.getColumn();
            String tableName = columnData.getTable();
            String tableAlias = columnData.getTableAlias();
            if(!TypeUtil.isEmpty(tableAlias)) tableName = tableAlias;
           return new Condition(tableName+"."+ tenantIdField.getColumn()  +" = " + GlobalConfig.getTenantFactory().getTenantId());
        }
        return null;
    }

    /**
     * 取得tenantId语句
     * @param table Table
     * @return Condition
     */
    private Condition getLogicDeleteCondition(Table table){
        String entityName = table.getColumn().getEntityName();
        EntityMap entityMap = EntityMapKids.getEntityMap(entityName);
        assert entityMap != null;
        EntityFieldMap logicDeleteField = entityMap.getLogicDeleteFieldMap();
        if(logicDeleteField != null){
            ColumnData columnData = table.getColumn();
            String tableName = columnData.getTable();
            String tableAlias = columnData.getTableAlias();
            if(!TypeUtil.isEmpty(tableAlias)) tableName = tableAlias;
            String value = logicDeleteField.getLogicNotDeleteValue();
            return new Condition(tableName+"."+ logicDeleteField.getColumn() + (value.equals("null")? " IS NULL":" = "+ value));
        }
        return null;
    }

    public QueryWrapper join(Table table, Condition condition){
        sqlStatement.join.add(formatJoin(table, condition));
        return this;
    }

    /**
     * 添加 prepareStatement 的 parameter参数
     * @param key 键名
     * @param value 键值
     * @return QueryWrapper
     */
    public QueryWrapper addParameter(String key, Object value){
        this.sqlStatement.parameterMap.put(key, value);
        return this;
    }

    public QueryWrapper innerJoin(Table table, Condition condition){
        sqlStatement.innerJoin.add(formatJoin(table, condition));
        return this;
    }

    public QueryWrapper leftJoin(Table table, Condition condition){
        sqlStatement.leftOuterJoin.add(formatJoin(table, condition));
        return this;
    }

    public QueryWrapper rightJoin(Table table, Condition condition){
        sqlStatement.rightOuterJoin.add(formatJoin(table, condition));
        return this;
    }

    public QueryWrapper union(QueryWrapper unionWrapper){
        sqlStatement.union.add(unionWrapper.getSql());
        return this;
    }

    public QueryWrapper unionAll(QueryWrapper unionWrapper){
        sqlStatement.unionAll.add(unionWrapper.getSql());
        return this;
    }

    public QueryWrapper where(Condition condition){
        return where(true, condition);
    }

    public QueryWrapper where(boolean apply, Condition condition){
        if(apply) {
            sqlStatement.where.add(condition.getSql());
            sqlStatement.parameterMap.putAll(condition.getParameterMap());
        }
        return this;
    }

    public QueryWrapper where(String condition){
        return where(true, condition);
    }

    public QueryWrapper where(boolean apply, String condition){
        if(apply) {
            sqlStatement.where.add(condition);
        }
        return this;
    }

    public QueryWrapper limit(Long offset, Long limit){
        sqlStatement.offset = offset;
        sqlStatement.limit = limit;
        return this;
    }

    public QueryWrapper limit(Long limit){
        sqlStatement.limit = limit;
        return this;
    }

    public QueryWrapper having(Condition condition){
        sqlStatement.having.add(condition.getSql());
        return this;
    }
    /**
     * 查询的数据字段列表
     * @param columns 数据列
     * @return QueryWrapper
     */
    public QueryWrapper select(Object ...columns) {
        sqlStatement.statementType = StatementType.SELECT;
        String columnName = "";
        for (Object column : columns
        ) {
            if (column instanceof Column) {
                List<String> columnList = ((Column) column).getAllColumns();
                sqlStatement.select.addAll(columnList);
            } else {
                columnName = column.toString();
                if (!sqlStatement.select.contains(columnName)) sqlStatement.select.add(columnName);
            }
        }
        return this;
    }

    /**
     * 从表删除
     * @param table 表
     * @return QueryWrapper
     */
    public QueryWrapper deleteFrom(Table table) {
        sqlStatement.statementType = StatementType.DELETE;

        sqlStatement.tableList.add(table);
        sqlStatement.tables.add(table.getFullTable());

        Condition logicDeleteCondition = getLogicDeleteCondition(table);
        if (logicDeleteCondition != null) where(logicDeleteCondition);

        Condition tenantCondition = getTenantCondition(table);
        if (tenantCondition != null) where(tenantCondition);


        return this;
    }

    /**
     * groupBy
     * @param columns 数据列
     * @return QueryWrapper
     */
    public QueryWrapper groupBy(Object ...columns) {
        String columnName = "";
        for (Object column : columns
        ) {
            if (column instanceof Column columnInstance) {
                columnInstance.removeLastColumn();
                columnName = columnInstance.getFullColumn();
                sqlStatement.groupBy.add(columnName);
            } else {
                columnName = column.toString();
                if (!sqlStatement.groupBy.contains(columnName)) sqlStatement.groupBy.add(columnName);
            }
        }
        return this;
    }

    /**
     * 去重
     * @return QueryWrapper
     */
    public QueryWrapper distinct(){
        sqlStatement.distinct = true;
        return this;
    }

    public QueryWrapper insertInto(String tableName) {
        sqlStatement.statementType = StatementType.INSERT;
        sqlStatement.tables.add(tableName);
        return this;
    }


    public QueryWrapper insertInto(Table table) {
        sqlStatement.statementType = StatementType.INSERT;
        sqlStatement.tables.add(table.getFullTable());
        return this;
    }


    /**
     * 插入的数据字段列表
     * @param columns 数据列
     * @return QueryWrapper
     */
    public QueryWrapper columns(Object ...columns) {
        String columnName = "";
        for (Object column : columns
        ) {
            if (column instanceof Column) {
                List<String> columnList = ((Column) column).getAllColumns();
                sqlStatement.columns.addAll(columnList);
            } else {
                columnName = column.toString();
                if (!sqlStatement.columns.contains(columnName)) sqlStatement.columns.add(columnName);
            }
        }
        return this;
    }


    /**
     * 插入的数据值列表
     * @param valuesList 数据值,如：(a,b,c),(a,b,c)
     * @return QueryWrapper
     */
    public QueryWrapper valuesList(List<List<String>> valuesList) {
       sqlStatement.valuesList.addAll(valuesList);
        return this;
    }

    /**
     * 修改语句中的 set 项。
     * @param values 如：key=value
     * @return QueryWrapper
     */
    public QueryWrapper setValues(List<String> values) {
        sqlStatement.sets.addAll(values);
        return this;
    }

    public QueryWrapper update(String table) {
        sqlStatement.statementType = StatementType.UPDATE;
        sqlStatement.tables.add(table);
        return this;
    }


    public QueryWrapper update(Table table) {
        sqlStatement.statementType = StatementType.UPDATE;
        sqlStatement.tables.add(table.getFullTable());
        return this;
    }

    public QueryWrapper set(String... sets) {
        sqlStatement.sets.addAll(Arrays.asList(sets));
        return this;
    }

    public String getSql(){
        return sqlStatement.sql(new StringBuilder(), false);
    }
    public Map<String, Object> getParameterMap(){
        return sqlStatement.parameterMap;
    }

    public String getSqlPaginate(){
        return sqlStatement.sql(new StringBuilder(), true);
    }

    public QueryWrapper from(Table... tables) {
        sqlStatement.tableList.addAll(Arrays.asList(tables));
        for (Table table : tables
        ) {
            sqlStatement.tables.add(table.getFullTable());
            Condition logicDeleteCondition = getLogicDeleteCondition(table);
            if (logicDeleteCondition != null) where(logicDeleteCondition);

            Condition tenantCondition = getTenantCondition(table);
            if (tenantCondition != null) where(tenantCondition);
        }
        return this;
    }


    private static class SafeAppendable {
        private final Appendable appendable;
        private boolean empty = true;

        public SafeAppendable(Appendable a) {
            this.appendable = a;
        }

        public SafeAppendable append(CharSequence s) {
            try {
                if (empty && s.length() > 0) {
                    empty = false;
                }
                appendable.append(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public boolean isEmpty() {
            return empty;
        }
    }

    public boolean hasSelect(){
        return sqlStatement.select.size()>0;
    }
    public boolean hasWhere(){
        return sqlStatement.where.size()>0;
    }

    public boolean hasTable(){
        return sqlStatement.tableList.size()>0;
    }
    private static class SQLStatement {

        StatementType statementType;
        List<String> sets = new ArrayList<>();
        List<String> select = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        List<Table> tableList = new ArrayList<>();
        List<String> join = new ArrayList<>();
        List<String> innerJoin = new ArrayList<>();
        List<String> outerJoin = new ArrayList<>();
        List<String> leftOuterJoin = new ArrayList<>();
        List<String> rightOuterJoin = new ArrayList<>();
        List<String> where = new ArrayList<>();
        List<String> having = new ArrayList<>();
        List<String> groupBy = new ArrayList<>();
        List<String> orderBy = new ArrayList<>();
        List<String> lastList = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        List<String> union = new ArrayList<>();
        List<String> unionAll = new ArrayList<>();

        List<List<String>> valuesList = new ArrayList<>();

        Map<String, Object> parameterMap = new HashMap<>();
        boolean distinct;
        Long offset;
        Long limit;

        boolean isPaginate;

        public SQLStatement() {
        }

        private void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open, String close,
                               String conjunction) {
            if (!parts.isEmpty()) {
                if (!builder.isEmpty()) {
                    builder.append("\n");
                }
                builder.append(keyword);
                builder.append(" ");
                builder.append(open);
                String last = "________";
                for (int i = 0, n = parts.size(); i < n; i++) {
                    String part = parts.get(i);
                    String AND = ") \nAND (";
                    String OR = ") \nOR (";
                    if (i > 0 && !part.equals(AND) && !part.equals(OR) && !last.equals(AND) && !last.equals(OR)) {
                        builder.append(conjunction);
                    }
                    builder.append(part);
                    last = part;
                }
                builder.append(close);
            }
        }

        private String selectSQL(SafeAppendable builder) {
            String foundRows = this.isPaginate? Sql.SPACE + "SQL_CALC_FOUND_ROWS" + Sql.SPACE: "";
            if (distinct) {
                sqlClause(builder, "SELECT DISTINCT" + foundRows, select, "", "", ", ");
            } else {
                sqlClause(builder, "SELECT"+ foundRows, select, "", "", ", ");
            }

            sqlClause(builder, "FROM", tables, "", "", ", ");
            joins(builder);
            wheres(builder);
            sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
            havings(builder);
            sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");
            limits(builder);
            unions(builder);
            if(this.isPaginate) builder.append(";\nselect FOUND_ROWS() as total;");
            return builder.appendable.toString();
        }

        private void limits(SafeAppendable builder) {
            if (limit == null) return;
            builder.append(Sql.SPACE).append("LIMIT").append(Sql.SPACE);
            if (offset != null) builder.append(offset + "," + Sql.SPACE);
            builder.append(limit.toString());
        }

        private void joins(SafeAppendable builder) {
            sqlClause(builder, "JOIN", join, "", "", "\nJOIN ");
            sqlClause(builder, "INNER JOIN", innerJoin, "", "", "\nINNER JOIN ");
            sqlClause(builder, "OUTER JOIN", outerJoin, "", "", "\nOUTER JOIN ");
            sqlClause(builder, "LEFT OUTER JOIN", leftOuterJoin, "", "", "\nLEFT OUTER JOIN ");
            sqlClause(builder, "RIGHT OUTER JOIN", rightOuterJoin, "", "", "\nRIGHT OUTER JOIN ");
        }

        private void unions(SafeAppendable builder){
            sqlClause(builder, "UNION", union, "(", ")", "\n) UNION (");
            sqlClause(builder, "UNION ALL", unionAll, "(", ")", "\n) UNION ALL (");
        }

        private void wheres(SafeAppendable builder) {
            if (where.isEmpty()) return;
            builder.append(Sql.SPACE + "WHERE" + Sql.SPACE);
            for (int i = 0; i < where.size(); i++) {
                String condition = where.get(i);
                if (i > 0) builder.append(Sql.SPACE).append("AND").append(Sql.SPACE);
                builder.append(SqlUtil.needBracket(condition) ? "(" + condition + ")" : condition);
            }
        }

        private void havings(SafeAppendable builder) {
            if (having.isEmpty()) return;
            builder.append(Sql.SPACE + "HAVING" + Sql.SPACE);
            for (int i = 0; i < having.size(); i++) {
                String condition = having.get(i);
                if (i > 0) builder.append(Sql.SPACE).append("AND").append(Sql.SPACE);
                builder.append(SqlUtil.needBracket(condition) ? "(" + condition + ")" : condition);
            }
        }

        /**
         * 表别名处理
         * @param sql
         * @return
         */
        private String replaceAlias(String sql) {
            int tableCount = this.tableList.size();
            if (tableCount <= 0) return sql;

            for (Table table : this.tableList
            ) {
                String tableAlias = table.getColumn().getTableAlias();
                String tableName = table.getColumn().getTable();
                String target = tableAlias + "\\.";
                if (TypeUtil.isEmpty(tableAlias)) {
                    if (tableCount == 1) target = "";
                    else target = tableName + "\\.";
                }
                sql = sql.replaceAll(tableName + "\\.", target);
            }

            return sql;
        }

        private String insertSQL(SafeAppendable builder) {
            sqlClause(builder, "INSERT INTO", tables, "", "", "");
            sqlClause(builder, "", columns, "(", ")", ", ");
            for (int i = 0; i < valuesList.size(); i++) {
                sqlClause(builder, (i > 0) ? "," : "VALUES", valuesList.get(i), "(", ")", ", ");
            }
            return builder.appendable.toString();
        }

        private String deleteSQL(SafeAppendable builder) {
            sqlClause(builder, "DELETE FROM", tables, "", "", "");
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            limits(builder);
            return builder.appendable.toString();
        }

        private String updateSQL(SafeAppendable builder) {
            sqlClause(builder, "UPDATE", tables, "", "", "");
            joins(builder);
            sqlClause(builder, "SET", sets, "", "", ", ");
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            limits(builder);
            return builder.appendable.toString();
        }

        public String sql(Appendable a, boolean isPaginate) {
            this.isPaginate = isPaginate;
            SafeAppendable builder = new SafeAppendable(a);
            if (statementType == null) {
                return null;
            }

            String sql =  switch (statementType) {
                case DELETE -> deleteSQL(builder);
                case INSERT -> insertSQL(builder);
                case SELECT, COUNT -> selectSQL(builder);
                case UPDATE -> updateSQL(builder);
            };
            return replaceAlias(sql);
        }
    }
}
