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
import com.mybatiseasy.generator.pojo.ColumnAutoSet;
import com.mybatiseasy.generator.pojo.ColumnInfo;
import com.mybatiseasy.generator.pojo.JavaDataType;
import com.mybatiseasy.generator.pojo.TableInfo;
import com.mybatiseasy.generator.utils.TypeConvert;
import com.mybatiseasy.generator.utils.Utils;
import com.mybatiseasy.keygen.IKeyGenerator;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class BaseDialect {

    protected DataSourceConfig dataSourceConfig;
    protected GlobalConfig globalConfig;
    protected EntityConfig entityConfig;

    protected final Map<String, Class<? extends IKeyGenerator>> keyGeneratorMap = new HashMap<>();

    protected String schema = "";

    protected String queryTableListSql = "";

    protected String queryColumnListSql = "";

    protected String tableNameColumn;

    protected String tableCommentColumn;

    protected String tableExtraColumn;

    protected String columnNameColumn;

    protected String columnCommentColumn;
    protected String columnTypeColumn;
    protected String columnScaleColumn;
    protected String columnKeyColumn;
    protected String columnExtraColumn;

    protected List<String> ignoreList = new ArrayList<>();

    protected List<TableInfo> getTableList(Connection conn) {
        return new ArrayList<>();
    }

    public BaseDialect() {
        ignoreList.add("sqlite_sequence");
    }

    /**
     * 表名映射为类名
     * @param tableName 表名
     * @return name
     */
    private String getName(String tableName){
        String name = tableName;
        for (String prefix: entityConfig.getPrefix()
        ) {
            if(tableName.startsWith(prefix)) {
                name = tableName.substring(prefix.length());
                break;
            }
        }
        return Utils.snakeToCamel(name);
    }

    /**
     * 取得所有表信息，未包含字段信息和主键信息
     *
     * @param conn Connection
     * @return List<TableInfo>
     */
    protected List<TableInfo> getOriginTableList(Connection conn) {
        try {
            assert conn != null;
            try (Statement statement = conn.createStatement()) {
                String sql = getSql(queryTableListSql, "");
                try (ResultSet rs = statement.executeQuery(sql)) {
                    List<TableInfo> tableInfoList = new ArrayList<>();
                    while (rs.next()) {
                        String tableName = rs.getString(tableNameColumn);
                        if (ignoreList.stream().anyMatch(item -> item.equals(tableName))) continue;

                        String tableComment = Utils.isNotEmpty(tableCommentColumn) ? rs.getString(tableCommentColumn) : "";
                        String extra = Utils.isNotEmpty(tableExtraColumn) ? rs.getString(tableExtraColumn) : "";
                        TableInfo tableInfo = new TableInfo();
                        tableInfo.setSchema(schema);
                        tableInfo.setTableName(tableName);
                        tableInfo.setComment(tableComment);
                        tableInfo.setExtra(extra);
                        tableInfo.setName(getName(tableName));
                        tableInfoList.add(tableInfo);
                    }
                    return tableInfoList;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库表查询失败" + e.getMessage());
        }
    }

    private String getSql(String query, String tableName) {
        return query.replace("#{schema}", "'" + schema + "'").replace("#{tableName}", "'" + tableName + "'");
    }

    protected List<ColumnInfo> getOriginColumnInfo(Connection conn, String tableName) {

        List<ColumnInfo> columnInfoList = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            String sql = getSql(queryColumnListSql, tableName);
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    String columnName = rs.getString(columnNameColumn);
                    String columnComment = Utils.isNotEmpty(this.columnCommentColumn) ? rs.getString(columnCommentColumn) : "";
                    ColumnInfo columnInfo = new ColumnInfo();
                    columnInfo.setName(Utils.snakeToCamel(columnName));
                    columnInfo.setComment(columnComment);
                    columnInfo.setColumnName(columnName);
                    columnInfo.setDataType(rs.getString(columnTypeColumn));
                    JavaDataType javaType = TypeConvert.fromDbType(columnInfo.getDataType());
                    columnInfo.setJavaType(javaType);
                    columnInfo.setJavaTypeName(javaType.getName());

                    if(Objects.equals(entityConfig.getVersionName(), columnInfo.getName())) {
                        columnInfo.setVersion(true);
                    }
                    if(Objects.equals(entityConfig.getTenantIdName(), columnInfo.getName())) {
                        columnInfo.setTenantId(true);
                    }
                    if(Objects.equals(entityConfig.getLogicDeleteName(), columnInfo.getName())) {
                        columnInfo.setLogicDelete(true);
                        columnInfo.setLogicDeleteValue(entityConfig.getLogicDeleteValue());
                    }

                    columnInfo.setColumnKey(Utils.isNotEmpty(this.columnKeyColumn) ? rs.getString(this.columnKeyColumn) : "");

                    columnInfo.setExtra(Utils.isNotEmpty(this.columnExtraColumn) ? rs.getString(this.columnExtraColumn) : "");
                    columnInfo.setNumericScale(Utils.isNotEmpty(this.columnScaleColumn) ? rs.getInt(this.columnScaleColumn) : 0);


                    ColumnAutoSet columnAutoSet = entityConfig.getColumnAutoSetList().stream().filter(item -> item.getName().equals(columnInfo.getName())).findAny().orElse(null);
                    if (columnAutoSet != null) {
                        if (columnAutoSet.getInsert() != null) columnInfo.setInsert(columnAutoSet.getInsert());
                        if (columnAutoSet.getUpdate() != null) columnInfo.setUpdate(columnAutoSet.getUpdate());
                    }
                    columnInfoList.add(columnInfo);
                }
                return columnInfoList;
            }
        } catch (Exception e) {
            throw new RuntimeException("数据表的字段生成失败:" + e.getMessage());
        }
    }


}
