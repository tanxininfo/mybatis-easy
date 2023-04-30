package com.mybatiseasy.generator.pojo;

import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.keygen.IKeyGenerator;

import java.util.List;

/**
 * <p>
 * 表字段
 * </p>
 *
 * @author dudley
 * @since 2022-05-09
 */
public class TableInfo {

    private String schema;

    private String name;

    private String tableName;

    private String comment;

    private List<ColumnInfo> columns;

    private String pri;

    private Class<? extends IKeyGenerator> keyGenerator;

    public String getPri() {
        return pri;
    }

    public Class<? extends IKeyGenerator> getKeyGenerator() {
        return keyGenerator;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    public String getComment() {
        return comment;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public TableInfo setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public TableInfo setName(String name) {
        this.name = name;
        return this;
    }



    public TableInfo setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setKeyGenerator(Class<? extends IKeyGenerator> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
