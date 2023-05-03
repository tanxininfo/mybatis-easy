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
