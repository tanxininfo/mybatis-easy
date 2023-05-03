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
     * 字段名称，驼峰
     */
    private String name;

    /**
     * 字段描述
     */
    private String comment;

    /**
     * 字段
     */
    private String columnName;
    /**
     * 是否主键
     */
    private boolean pri;

    private int numericScale;

    private boolean autoIncrement;

    private String dataType;
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

    private String columnKey;

    private String extra;

    public String getColumnKey() {
        return columnKey;
    }

    public String getExtra() {
        return extra;
    }

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


    public String getDataType() {
        return dataType;
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

    public void setDataType(String dataType) {
        this.dataType = dataType;
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

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
