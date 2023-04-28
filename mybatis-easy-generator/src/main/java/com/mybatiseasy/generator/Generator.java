package com.mybatiseasy.generator;

import com.google.protobuf.Enum;
import com.mybatiseasy.generator.config.*;
import com.mybatiseasy.generator.pojo.ColumnInfo;
import com.mybatiseasy.generator.pojo.MysqlDataType;
import com.mybatiseasy.generator.pojo.TableInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public Generator dataSourceConfig(DataSourceConfig dataSourceConfig){
         this.dataSourceConfig = dataSourceConfig;
        return this;
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
        Assert.hasLength(dataSourceConfig.getUsername(), "数据库配置 username 不得为空");
        Assert.hasLength(dataSourceConfig.getPassword(), "数据库配置 password 不得为空");
        try {
            return DriverManager.getConnection(dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());

        } catch (Exception ex) {
            Assert.isTrue(true, "数据库连接配置有误");
        }
        return null;
    }

    public void generate(){
        List<TableInfo> tableInfoList = getTableList();
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
            throw new RuntimeException("数据库表查询失败");
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
                tableInfo.setName(tableName);
                tableInfo.setComment(tableComment);
                List<ColumnInfo> columnInfoList = formatToColumnInfo(schema, tableName, conn);
                tableInfo.setColumnInfoList(columnInfoList);
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
                columnInfo.setName(columnName);
                columnInfo.setComment(columnComment);
                columnInfo.setDataType(MysqlDataType.valueOf(rs.getString("DATA_TYPE").toUpperCase()));
                columnInfoList.add(columnInfo);
            }
            rs.close();
            return columnInfoList;
        } catch (Exception e) {
            throw new RuntimeException("数据表的字段生成失败:"+ e.getMessage());
        }
    }
}
