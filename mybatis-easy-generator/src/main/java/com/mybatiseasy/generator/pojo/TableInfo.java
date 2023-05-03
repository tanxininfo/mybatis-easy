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

    /**
     * 用以存放其他信息
     */
    private String extra;


    public String getExtra() {
        return extra;
    }

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

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
