/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mybatiseasy.core.paginate;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询分页信息
 *
 */
@Data
public class Page implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 数据总数
     */
    private long total;

    /**
     * 每页记录数
     */
    private long size;

    /**
     * 当前页
     */
    private long current;

    /**
     * 页数
     */
    private long pages;

    public Page(long total, long size, long current){
        this.setCurrent(current);
        this.setTotal(total);
        this.setSize(size);
        this.setPages((long) Math.ceil(this.getTotal() * 1.0/this.getSize()));
    }
}
