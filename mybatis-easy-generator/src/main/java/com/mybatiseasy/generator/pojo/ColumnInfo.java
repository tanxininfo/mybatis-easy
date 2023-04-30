package com.mybatiseasy.generator.pojo;


import com.sun.jdi.PrimitiveValue;

/**
 * <p>
 * 表字段
 * </p>
 *
 * @author dudley
 * @since 2022-05-09
 */
public class ColumnInfo {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段描述
     */
    private String comment;

    /**
     * 字段描述
     */
    private String columnName;
    /**
     * 是否主键
     */
    private boolean pri;

    private int numericScale;

    private boolean autoIncrement;

    private String dbType;
    private JavaDataType javaType;
    private String javaTypeName;

    /**
     * 新增记录时自动填充的值
     */
    private String insert;

    /**
     * 修改记录时自动填充的值
     */
    private String update;

    public String getInsert() {
        return insert;
    }

    public String getUpdate() {
        return update;
    }

    public String getColumnName() {
        return columnName;
    }

    public JavaDataType getJavaType() {
        return javaType;
    }


    public String getJavaTypeName() {
        return javaTypeName;
    }


    public String getDbType() {
        return dbType;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public int getNumericScale() {
        return numericScale;
    }

    public boolean isPri() {
        return pri;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPri(boolean pri) {
        this.pri = pri;
    }

    public void setNumericScale(int numericScale) {
        this.numericScale = numericScale;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public void setJavaType(JavaDataType javaType) {
        this.javaType = javaType;
    }

    public void setJavaTypeName(String javaTypeName) {
        this.javaTypeName = javaTypeName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
