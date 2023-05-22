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

package com.mybatiseasy.generator.dialect;

import com.mybatiseasy.generator.config.DataSourceConfig;
import com.mybatiseasy.generator.config.EntityConfig;
import com.mybatiseasy.generator.config.GlobalConfig;
import com.mybatiseasy.generator.pojo.ColumnInfo;
import com.mybatiseasy.generator.pojo.TableInfo;

import java.sql.Connection;
import java.util.List;


public class MySQLDialect extends BaseDialect implements IDialect {

    public MySQLDialect(DataSourceConfig dataSourceConfig, GlobalConfig globalConfig, EntityConfig entityConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.globalConfig = globalConfig;
        this.entityConfig = entityConfig;
        this.schema = dataSourceConfig.getSchema();

        this.queryTableListSql = "select * from information_schema.TABLES where TABLE_SCHEMA=#{schema}";
        this.queryColumnListSql = "select * from information_schema.COLUMNS where TABLE_SCHEMA=#{schema} and TABLE_NAME=#{tableName}";

        this.tableNameColumn = "TABLE_NAME";
        this.tableCommentColumn = "TABLE_COMMENT";
        this.tableExtraColumn = "";

        this.columnNameColumn = "COLUMN_NAME";
        this.columnCommentColumn = "COLUMN_COMMENT";
        this.columnTypeColumn = "COLUMN_TYPE";
        this.columnScaleColumn = "NUMERIC_SCALE";
        this.columnKeyColumn = "COLUMN_KEY";
        this.columnExtraColumn = "EXTRA";
    }

    public List<TableInfo> getTableList(Connection conn) {
        try (conn) {
            assert conn != null;

            List<TableInfo> tableInfoList = getOriginTableList(conn);
            String tableName;

            for (TableInfo tableInfo : tableInfoList) {
                tableName = tableInfo.getTableName();
                List<ColumnInfo> columns = getOriginColumnInfo(conn, tableName);
                this.formatColumnInfo(tableName, columns, tableInfo.getExtra());
                tableInfo.setColumns(columns);
                this.formatTableInfo(tableInfo, columns);
            }
            return tableInfoList;

        } catch (Exception e) {
            throw new RuntimeException("数据库表查询失败" + e.getMessage());
        }
    }

    /**
     * 格式化表信息，如pri
     * @param tableInfo 表信息
     * @param columns 字段列表
     */
    public void formatTableInfo(TableInfo tableInfo, List<ColumnInfo> columns) {
        columns.stream().filter(ColumnInfo::isPri).findFirst().ifPresent(tableInfo::setPriColumn);
        if (keyGeneratorMap.containsKey(tableInfo.getName())) {
            tableInfo.setKeyGenerator(globalConfig.getKeyGenerator());
        }
    }

        /**
         * 格式化字段列表，处理相关数据,如pri和 autoIncrement等
         * @param table 表名，如：sysUser
         * @param columns 字段列表
         */
    public void formatColumnInfo(String table, List<ColumnInfo> columns, String extra) {
        for (ColumnInfo columnInfo : columns) {
            columnInfo.setPri(columnInfo.getColumnKey().toUpperCase().contains("PRI"));
            columnInfo.setAutoIncrement(columnInfo.getExtra().toLowerCase().contains("auto_increment"));
            if (columnInfo.isPri() && !columnInfo.isAutoIncrement() && (globalConfig.getKeyGenerator() != null)) {
                keyGeneratorMap.put(table, globalConfig.getKeyGenerator());
            }
        }
    }
}
