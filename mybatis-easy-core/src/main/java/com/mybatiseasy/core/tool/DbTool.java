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

package com.mybatiseasy.core.tool;

import com.mybatiseasy.core.config.GlobalConfig;
import com.mybatiseasy.core.mapper.DbMapper;
import com.mybatiseasy.core.paginate.PageList;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.type.Record;
import com.mybatiseasy.core.type.RecordList;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DbTool {

    private static <R> R run(Function<DbMapper, R> function) {
        SqlSessionFactory sqlSessionFactory = GlobalConfig.getSqlSessionFactory();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            DbMapper mapper = sqlSession.getMapper(DbMapper.class);
            return function.apply(mapper);
        }
    }

    public static int insert(Record record, Class<?> entityClass) {
        return run(mapper -> mapper.insert(record, entityClass));
    }

    public static int insertBySql(String sql) {
        return run(mapper -> mapper.insertBySql(sql));
    }

    public static int insertBatch(List<Record> recordList, Class<?> entityClass) {
        return run(mapper -> mapper.insertBatch(recordList, entityClass));
    }

    public static <T> PageList<T> paginate(QueryWrapper queryWrapper, int size, int current, Class<T> entityClass) {
        return run(mapper -> mapper.paginate(queryWrapper, size, current, entityClass));
    }

    public static RecordList list(QueryWrapper wrapper) {
        return run(mapper -> mapper.list(wrapper));
    }

    public static RecordList listBySql(String sql) {
        return run(mapper -> mapper.list(sql));
    }

    public static Record getSingle(QueryWrapper wrapper) {
        return run(mapper -> mapper.getSingle(wrapper));
    }

    public static Record getOne(QueryWrapper wrapper) {
        return run(mapper -> mapper.getOne(wrapper));
    }


    public static Record getBySql(String sql) {
        return run(mapper -> mapper.getBySql(sql));
    }


    public static long count(QueryWrapper wrapper) {
        return run(mapper -> mapper.count(wrapper));
    }

    public static int delete(QueryWrapper wrapper) {
        return run(mapper -> mapper.delete(wrapper));
    }

    public static int deleteBySql(String sql) {
        return run(mapper -> mapper.deleteBySql(sql));
    }

    public static int delete(QueryWrapper wrapper, boolean force) {
        return run(mapper -> mapper.delete(wrapper, force));
    }

    public static int update(Record record, QueryWrapper wrapper) {
        return run(mapper -> mapper.update(record, wrapper));
    }


    public static int updateBySql(String sql) {
        return run(mapper -> mapper.updateBySql(sql));
    }

}