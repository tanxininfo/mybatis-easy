/*
 *
 *  *
 *  *  * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package com.mybatiseasy.generator;

import com.mybatiseasy.generator.config.*;
import com.mybatiseasy.generator.dialect.DialectFactory;
import com.mybatiseasy.generator.dialect.IDialect;
import com.mybatiseasy.generator.pojo.*;
import com.mybatiseasy.generator.template.FreemarkerTemplate;
import com.mybatiseasy.generator.template.ITemplate;
import com.mybatiseasy.generator.utils.TypeConvert;
import com.mybatiseasy.generator.utils.Utils;
import com.mybatiseasy.keygen.IKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Generator {
    private DataSourceConfig dataSourceConfig;
    private GlobalConfig globalConfig;
    private EntityConfig entityConfig;
    private MapperConfig mapperConfig;
    private ServiceConfig serviceConfig;
    private ServiceImplConfig serviceImplConfig;
    private DtoConfig dtoConfig;
    private ControllerConfig controllerConfig;

    private ITemplate templateEngine;

    private final Map<String, Class<? extends IKeyGenerator>> keyGeneratorMap = new HashMap<>();


    public Generator dataSourceConfig(DataSourceConfig dataSourceConfig){
         this.dataSourceConfig = dataSourceConfig;
        return this;
    }

    public void templateEngine(ITemplate templateEngine) {
        this.templateEngine = templateEngine;
    }

    public Generator globalConfig(GlobalConfig globalConfig){
        this.globalConfig = globalConfig;
        return this;
    }
    public Generator entityConfig(EntityConfig entityConfig){
        this.entityConfig = entityConfig;
        return this;
    }
    public Generator mapperConfig(MapperConfig mapperConfig){
        this.mapperConfig = mapperConfig;
        return this;
    }
    public Generator serviceConfig(ServiceConfig serviceConfig){
        this.serviceConfig = serviceConfig;
        return this;
    }
    public Generator serviceImplConfig(ServiceImplConfig serviceImplConfig){
        this.serviceImplConfig = serviceImplConfig;
        return this;
    }
    public Generator controllerConfig(ControllerConfig controllerConfig){
        this.controllerConfig = controllerConfig;
        return this;
    }
    public Generator dtoConfig(DtoConfig dtoConfig){
        this.dtoConfig = dtoConfig;
        return this;
    }


    private Connection getConnection() {
        Assert.hasLength(dataSourceConfig.getUrl(), "数据库配置 url 不得为空");
        try {
            return DriverManager.getConnection(dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());

        } catch (Exception ex) {
            Assert.isTrue(true, "数据库连接配置有误");
        }
        return null;
    }

    public void generate() {
        this.initConfig();
        this.templateEngine.init(globalConfig,
                entityConfig,
                dtoConfig,
                mapperConfig,
                controllerConfig,
                serviceConfig,
                serviceImplConfig);

        IDialect dialect = DialectFactory.getDialect(dataSourceConfig, globalConfig, entityConfig);
        List<TableInfo> tableList = dialect.getTableList(getConnection());

        for (TableInfo tableInfo : tableList
        ) {
            if (wantToWrite(TemplateType.ENTITY)) this.templateEngine.writeEntity(tableInfo);
            if (wantToWrite(TemplateType.MAPPER)) this.templateEngine.writeMapper(tableInfo);
            if (wantToWrite(TemplateType.DTO)) this.templateEngine.writeDto(tableInfo);
        }
    }

    private void initConfig(){
        if(this.globalConfig== null){
            String rootPath = System.getProperty("user.dir");
            this.globalConfig = new GlobalConfig.Builder(rootPath, "com.mybatiseasy").build();
        }
        if(this.templateEngine == null){
            this.templateEngine = new FreemarkerTemplate();
        }
        if(this.entityConfig == null){
            this.entityConfig = new EntityConfig.Builder("entity", "Entity").build();
        }
        if(this.mapperConfig == null){
            this.mapperConfig = new MapperConfig.Builder("mapper", "Mapper").build();
        }
        if(this.dtoConfig == null){
            this.dtoConfig = new DtoConfig.Builder("dto", "Dto").build();
        }
    }

    /**
     * 是否需要生成文件
     * @param templateType 类型
     * @return boolean
     */
    private boolean wantToWrite(TemplateType templateType) {
        return globalConfig.getTemplateTypeList().contains(templateType) || globalConfig.getTemplateTypeList().contains(TemplateType.ALL);
    }


    private List<TableInfo> getTableList() {
        String sql = "select * from information_schema.TABLES where TABLE_SCHEMA=?";

        try (Connection conn = this.getConnection()) {
            assert conn != null;
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, dataSourceConfig.getSchema());
                ResultSet rs =  preparedStatement.executeQuery();
                List<TableInfo> tableInfoList =  formatToTableInfo(rs, conn);
                rs.close();
                return tableInfoList;
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库表查询失败"+e.getMessage());
        }
    }

    private List<TableInfo> formatToTableInfo(ResultSet recordSet, Connection conn){
        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            while (recordSet.next()) {
                String schema = recordSet.getString("TABLE_SCHEMA");
                String tableName = recordSet.getString("TABLE_NAME");
                String tableComment = recordSet.getString("TABLE_COMMENT");
                TableInfo tableInfo = new TableInfo();
                tableInfo.setSchema(schema);
                tableInfo.setTableName(tableName);
                tableInfo.setName(Utils.snakeToCamel(tableName));
                tableInfo.setComment(tableComment);
                List<ColumnInfo> columns = formatToColumnInfo(schema, tableName, conn);
                ColumnInfo priColumn = columns.stream().filter(ColumnInfo::isPri).findFirst().orElse(null);
                assert priColumn != null;
                tableInfo.setPri(priColumn.getName());
                tableInfo.setColumns(columns);
                if(keyGeneratorMap.containsKey(tableInfo.getName())){
                    tableInfo.setKeyGenerator(globalConfig.getKeyGenerator());
                }
                tableInfoList.add(tableInfo);
            }
            return tableInfoList;
        } catch (Exception e) {
            throw new RuntimeException("数据表Dto生成失败:"+ e.getMessage());
        }
    }

    private List<ColumnInfo> formatToColumnInfo(String schema, String tableName, Connection conn){
        String sql = "select * from information_schema.COLUMNS where TABLE_SCHEMA=? and TABLE_NAME=?";

        List<ColumnInfo> columnInfoList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, schema);
            preparedStatement.setString(2, tableName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnComment = rs.getString("COLUMN_COMMENT");
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setName(Utils.snakeToCamel(columnName));
                columnInfo.setComment(columnComment);
                columnInfo.setColumnName(columnName);
                columnInfo.setDataType(rs.getString("COLUMN_TYPE"));
                JavaDataType javaType = TypeConvert.fromDbType(columnInfo.getDataType());
                columnInfo.setJavaType(javaType);
                columnInfo.setJavaTypeName(javaType.getName());
                columnInfo.setPri(rs.getString("COLUMN_KEY").toUpperCase().contains("PRI"));
                columnInfo.setNumericScale(rs.getInt("NUMERIC_SCALE"));
                columnInfo.setAutoIncrement(rs.getString("EXTRA").toLowerCase().contains("auto_increment"));
                ColumnAutoSet columnAutoSet = entityConfig.getColumnAutoSetList().stream().filter( item -> item.getName().equals(columnInfo.getName())).findAny().orElse(null);
                if(columnAutoSet!= null){
                    if(columnAutoSet.getInsert()!=null) columnInfo.setInsert(columnAutoSet.getInsert());
                    if(columnAutoSet.getUpdate()!=null) columnInfo.setUpdate(columnAutoSet.getUpdate());
                }
                if(columnInfo.isPri() && !columnInfo.isAutoIncrement() && (globalConfig.getKeyGenerator()!=null)){
                    keyGeneratorMap.put(Utils.snakeToCamel(tableName), globalConfig.getKeyGenerator());
                }
                columnInfoList.add(columnInfo);
            }
            rs.close();
            return columnInfoList;
        } catch (Exception e) {
            throw new RuntimeException("数据表的字段生成失败:"+ e.getMessage());
        }
    }
}
