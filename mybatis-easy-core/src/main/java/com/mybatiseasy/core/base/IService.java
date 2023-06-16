/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mybatiseasy.core.base;


import com.mybatiseasy.core.paginate.PageList;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;

import java.io.Serializable;
import java.util.List;

public interface IService<T> {
    IMapper<T> getBaseMapper();

    default T getById(Serializable id){
        return this.getBaseMapper().getById(id);
    }
    default T getOne(Condition condition){
        return this.getBaseMapper().getOne(condition);
    }

    default T getOne(QueryWrapper queryWrapper){
        return this.getBaseMapper().getOne(queryWrapper);
    }

    default List<T> list(Condition condition){
        return this.getBaseMapper().list(condition);
    }

    default List<T> list(QueryWrapper queryWrapper){
        return this.getBaseMapper().list(queryWrapper);
    }

    default PageList<T> paginate(Condition condition, int size, int current) {
        return this.getBaseMapper().paginate(condition, size, current);
    }
    default PageList<T> paginate(QueryWrapper queryWrapper, int size, int current) {
        return this.getBaseMapper().paginate(queryWrapper, size, current);
    }


    default int update(T entity) {
        return this.getBaseMapper().update(entity);
    }
    default int update(T entity, QueryWrapper wrapper) {
        return this.getBaseMapper().update(entity, wrapper);
    }
    default int update(T entity, Condition condition){ return this.getBaseMapper().update(entity, QueryWrapper.create().where(condition)); }

    default int updateById(T entity){
        return this.update(entity);
    }

    default long count(QueryWrapper wrapper) {
        return this.getBaseMapper().countByWrapper(wrapper);
    }
    default long count(Condition condition){
        return this.getBaseMapper().countByWrapper(QueryWrapper.create().where(condition));
    }

    default int insert(T entity){
        return this.getBaseMapper().insert(entity);
    }


    default int deleteById(Serializable id) {
        return this.getBaseMapper().deleteById(id);
    }

    default int delete(Serializable id) {
        return this.getBaseMapper().deleteById(id);
    }
    default int delete(QueryWrapper wrapper) {
        return this.getBaseMapper().deleteByWrapper(wrapper, false);
    }
    default int delete(QueryWrapper wrapper, boolean force) {
        return this.getBaseMapper().deleteByWrapper(wrapper, force);
    }
    default int delete(Condition condition){ return this.getBaseMapper().deleteByWrapper(QueryWrapper.create().where(condition), false); }


}