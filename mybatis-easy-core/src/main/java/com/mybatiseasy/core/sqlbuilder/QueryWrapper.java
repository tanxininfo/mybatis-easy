package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.AbstractSQL;
import org.apache.ibatis.mapping.SqlCommandType;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
public class QueryWrapper implements Serializable {


    public static QueryWrapper create(){
        return new QueryWrapper();
    }

    private final SQLStatement sqlStatement = new SQLStatement();


    public QueryWrapper where(Condition condition){
        sqlStatement.where.add(condition.getSql());
        return this;
    }

    /**
     * 查询的数据字段列表
     * @param columns 数据列
     * @return QueryWrapper
     */
    public QueryWrapper select(Object ...columns){
        sqlStatement.statementType = SQLStatement.StatementType.SELECT;
        String columnName = "";
        for (Object column: columns
             ) {
            if(column instanceof  Condition){
                columnName = ((Condition) column).getColumn();
            }else{
                columnName = column.toString();
            }
            if(!sqlStatement.select.contains(columnName)) sqlStatement.select.add(columnName);
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

    public QueryWrapper from(Table... tables) {
        sqlStatement.tableList.addAll(Arrays.asList(tables));
        for (Table table: tables
             ) {
            sqlStatement.tables.add(table.getName()+ Sql.SPACE+ table.getAs());
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
        log.info("aaaa");
            sqlClause(builder, "FROM", tables, "", "", ", ");
            joins(builder);
            sqlClause(builder, "WHERE", where, "(", ")", " AND ");
            sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
            sqlClause(builder, "HAVING", having, "(", ")", " AND ");
            sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");
            limitingRowsStrategy.appendClause(builder, offset, limit);
            log.info("builder.toString()={}",builder.appendable.toString());
            return builder.appendable.toString();
        }

        private void joins(SafeAppendable builder) {
            sqlClause(builder, "JOIN", join, "", "", "\nJOIN ");
            sqlClause(builder, "INNER JOIN", innerJoin, "", "", "\nINNER JOIN ");
            sqlClause(builder, "OUTER JOIN", outerJoin, "", "", "\nOUTER JOIN ");
            sqlClause(builder, "LEFT OUTER JOIN", leftOuterJoin, "", "", "\nLEFT OUTER JOIN ");
            sqlClause(builder, "RIGHT OUTER JOIN", rightOuterJoin, "", "", "\nRIGHT OUTER JOIN ");
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
