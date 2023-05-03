/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

package com.mybatiseasy.core.consts;

public interface Method {

    String INSERT = "insert";
    String INSERT_BATCH = "insertBatch";
    String DELETE_BY_ID = "deleteById";
    String GET_BY_ID = "getById";
    String GET_BY_CONDITION = "getByCondition";
    String GET_ONE = "getOne";

    String GET_BY_WRAPPER = "getByWrapper";
    String LIST_BY_CONDITION = "listByCondition";
    String LIST_BY_WRAPPER = "listByWrapper";

    String LIST = "list";
    String COUNT_BY_CONDITION = "countByCondition";
    String COUNT_BY_WRAPPER = "countByWrapper";
    String COUNT = "count";
    String QUERY_EASY = "queryEasy";
    String DELETE_BY_WRAPPER = "deleteByWrapper";
    String DELETE_BY_CONDITION = "deleteByCondition";
    String DELETE = "delete";
    String UPDATE_BY_ID = "updateById";
    String UPDATE_BY_CONDITION = "updateByCondition";
    String UPDATE_BY_WRAPPER = "updateByWrapper";

    String UPDATE = "update";
}
