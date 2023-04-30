package com.mybatiseasy.generator.pojo;


/**
 * <p>
 * 自动插入值(新增或更新)的字段
 * </p>
 *
 * @author dudley
 * @since 2022-05-09
 */
public class ColumnAutoSet {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段描述
     */
    private String insert;

    /**
     * 字段描述
     */
    private String update;

    public String getName() {
        return name;
    }

    public String getInsert() {
        return insert;
    }

    public String getUpdate() {
        return update;
    }

    public ColumnAutoSet setName(String name) {
        this.name = name;
        return this;
    }

    public ColumnAutoSet setInsert(String insert) {
        this.insert = insert;
        return this;
    }

    public ColumnAutoSet setUpdate(String update) {
        this.update = update;
        return this;
    }
}
