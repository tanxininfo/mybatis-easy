package com.mybatiseasy.generator.pojo;

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

    private String comment;

    private List<ColumnInfo> columnInfoList;

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
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

    public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
    }
}
