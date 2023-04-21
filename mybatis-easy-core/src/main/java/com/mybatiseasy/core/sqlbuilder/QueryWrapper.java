package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
public class QueryWrapper implements Serializable {

    public QueryWrapper(){
    }

    public static QueryWrapper create(){
        return new QueryWrapper();
    }

    private final SQLStatement sqlStatement = new SQLStatement();


    public QueryWrapper orderBy(Column column, boolean isDesc){
        sqlStatement.orderBy.add(column.getTableColumn()+ Sql.SPACE + (isDesc? "DESC":"ASC"));
        return this;
    }

    private String formatJoin(Column column, Condition condition) {
        return column.getFullTable()+ Sql.SPACE + "ON" + Sql.SPACE + condition.getSql();
    }

    public QueryWrapper join(Column column, Condition condition){
        sqlStatement.join.add(formatJoin(column, condition));
        return this;
    }

    public QueryWrapper innerJoin(Column column, Condition condition){
        sqlStatement.innerJoin.add(formatJoin(column, condition));
        return this;
    }

    public QueryWrapper leftJoin(Column column, Condition condition){
        sqlStatement.leftOuterJoin.add(formatJoin(column, condition));
        return this;
    }

    public QueryWrapper rightJoin(Column column, Condition condition){
        sqlStatement.rightOuterJoin.add(formatJoin(column, condition));
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
        sqlStatement.where.add(condition.getSql());
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
        sqlStatement.statementType = SQLStatement.StatementType.SELECT;
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
     * groupBy
     * @param columns 数据列
     * @return QueryWrapper
     */
    public QueryWrapper groupBy(Object ...columns) {
        String columnName = "";
        for (Object column : columns
        ) {
            if (column instanceof Column) {
                List<String> columnList = ((Column) column).getAllColumns();
                sqlStatement.groupBy.addAll(columnList);
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



    public QueryWrapper update(String table) {
        sqlStatement.statementType = SQLStatement.StatementType.UPDATE;
        sqlStatement.tables.add(table);
        return this;
    }


    public QueryWrapper set(String... sets) {
        sqlStatement.sets.addAll(Arrays.asList(sets));
        return this;
    }

    public String getSql(){
        return sqlStatement.sql(new StringBuilder());
    }

    public QueryWrapper from(Column... tables) {
        sqlStatement.tableList.addAll(Arrays.asList(tables));
        for (Column table : tables
        ) {
            sqlStatement.tables.add(table.getFullTable());
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

    public boolean hasTable(){
        return sqlStatement.tables.size()>0;
    }
    private static class SQLStatement {

        public enum StatementType {

            DELETE,

            INSERT,

            SELECT,

            UPDATE

        }

        private enum LimitingRowsStrategy {
            NOP {
                @Override
                protected void appendClause(SafeAppendable builder, String offset, String limit) {
                    // NOP
                }
            },
            ISO {
                @Override
                protected void appendClause(SafeAppendable builder, String offset, String limit) {
                    if (offset != null) {
                        builder.append(" OFFSET ").append(offset).append(" ROWS");
                    }
                    if (limit != null) {
                        builder.append(" FETCH FIRST ").append(limit).append(" ROWS ONLY");
                    }
                }
            },
            OFFSET_LIMIT {
                @Override
                protected void appendClause(SafeAppendable builder, String offset, String limit) {
                    if (limit != null) {
                        builder.append(" LIMIT ").append(limit);
                    }
                    if (offset != null) {
                        builder.append(" OFFSET ").append(offset);
                    }
                }
            };

            protected abstract void appendClause(SafeAppendable builder, String offset, String limit);

        }

        SQLStatement.StatementType statementType;
        List<String> sets = new ArrayList<>();
        List<String> select = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        List<Column> tableList = new ArrayList<>();
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
        boolean distinct;
        String offset;
        String limit;
        SQLStatement.LimitingRowsStrategy limitingRowsStrategy = SQLStatement.LimitingRowsStrategy.NOP;

        public SQLStatement() {
            // Prevent Synthetic Access
            valuesList.add(new ArrayList<>());
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
            if (distinct) {
                sqlClause(builder, "SELECT DISTINCT", select, "", "", ", ");
            } else {
                sqlClause(builder, "SELECT", select, "", "", ", ");
            }

            sqlClause(builder, "FROM", tables, "", "", ", ");
            joins(builder);
            wheres(builder);
            sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
            sqlClause(builder, "HAVING", having, "(", ")", " AND ");
            sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");
            limitingRowsStrategy.appendClause(builder, offset, limit);
            unions(builder);
            return builder.appendable.toString();
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

        private void having(SafeAppendable builder) {
            if (having.isEmpty()) return;
            builder.append(Sql.SPACE + "HAVING" + Sql.SPACE);
            for (int i = 0; i < having.size(); i++) {
                String condition = having.get(i);
                if (i > 0) builder.append(Sql.SPACE).append("AND").append(Sql.SPACE);
                builder.append(SqlUtil.needBracket(condition) ? "(" + condition + ")" : condition);
            }
        }

        private String insertSQL(SafeAppendable builder) {
            sqlClause(builder, "INSERT INTO", tables, "", "", "");
            sqlClause(builder, "", columns, "(", ")", ", ");
            for (int i = 0; i < valuesList.size(); i++) {
                sqlClause(builder, i > 0 ? "," : "VALUES", valuesList.get(i), "(", ")", ", ");
            }
            return builder.toString();
        }

        private String deleteSQL(SafeAppendable builder) {
            sqlClause(builder, "DELETE FROM", tables, "", "", "");
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            limitingRowsStrategy.appendClause(builder, null, limit);
            return builder.toString();
        }

        private String updateSQL(SafeAppendable builder) {
            sqlClause(builder, "UPDATE", tables, "", "", "");
            joins(builder);
            sqlClause(builder, "SET", sets, "", "", ", ");
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            limitingRowsStrategy.appendClause(builder, null, limit);
            return builder.toString();
        }

        public String sql(Appendable a) {
            SafeAppendable builder = new SafeAppendable(a);
            if (statementType == null) {
                return null;
            }

            return switch (statementType) {
                case DELETE -> deleteSQL(builder);
                case INSERT -> insertSQL(builder);
                case SELECT -> selectSQL(builder);
                case UPDATE -> updateSQL(builder);
            };
        }
    }
}
