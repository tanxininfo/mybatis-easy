package com.mybatiseasy.generator.pojo;


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
     * 数据类型
     */
    private MysqlDataType dataType;


    /**
     * 字段描述
     */
    private String comment;

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public MysqlDataType getDataType() {
        return dataType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDataType(MysqlDataType dataType) {
        this.dataType = dataType;
    }
}
