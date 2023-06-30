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

package com.mybatiseasy.core.base;

import com.mybatiseasy.core.consts.Method;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.paginate.Page;
import com.mybatiseasy.core.paginate.PageList;
import com.mybatiseasy.core.paginate.Total;
import com.mybatiseasy.core.provider.DbProvider;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.type.Record;
import com.mybatiseasy.core.type.RecordList;
import com.mybatiseasy.core.utils.EntityMapUtil;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 非record实例的数据库操作类。
 * 注意，目前主要使用到查询方法，其他方法还没有修改正确。
 */
public interface IDbMapper {
    /**
     * 插入一个实体
     * @param record 实例
     * @return 插入行数
     */
    @InsertProvider(type = DbProvider.class, method = Method.INSERT)
    int insert(@Param(MethodParam.RECORD) Record record, @Param(MethodParam.ENTITY_CLASS) Class<?> entityClass);

    /**
     * 插入一组实体
     * @param recordList 实例列表
     * @return 插入行数
     */
    @InsertProvider(type = DbProvider.class, method = Method.INSERT_BATCH)
    int insertBatch(@Param(MethodParam.RECORD_LIST) List<Record> recordList, @Param(MethodParam.ENTITY_CLASS) Class<?> entityClass);


    /**
     * 修改一个实体
     * @param record 实例
     * @param queryWrapper 条件
     * @return 影响条数
     */
    @UpdateProvider(type = DbProvider.class, method = Method.UPDATE_BY_WRAPPER)
    int updateByWrapper(@Param(MethodParam.RECORD) Map<String, Object> record, @Param(MethodParam.WRAPPER) QueryWrapper queryWrapper);

    @DeleteProvider(type = DbProvider.class, method = Method.DELETE_BY_WRAPPER)
    int deleteByWrapper(@Param(MethodParam.WRAPPER) QueryWrapper queryWrapper, @Param(MethodParam.FORCE) boolean force);


    /**
     * 根据包装类查询一条实例
     * @param wrapper 查询包装类
     * @return List<Record>
     */
    @SelectProvider(type = DbProvider.class, method = Method.GET_BY_WRAPPER)
    Record getByWrapper(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);

    /**
     * 根据包装类查询实例列表
     * @param wrapper 查询包装类
     * @return List<Record>
     */
    @SelectProvider(type = DbProvider.class, method = Method.LIST_BY_WRAPPER)
    List<Record> listByWrapper(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);


    /**
     * 根据包装类统计实例数量
     * @param wrapper 查询包装类
     * @return List<Record>
     */
    @SelectProvider(type = DbProvider.class, method = Method.COUNT_BY_WRAPPER)
    Long countByWrapper(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);

    /**
     * 根据包装类分页查询,故意命名为query,避免干扰paginate方法名
     * @param wrapper 查询包装类
     * @return List<Record>
     */
    @SelectProvider(type = DbProvider.class, method = Method.QUERY_EASY)
    List<Map<String, Object>> queryEasy(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);


    /**
     * 分页查询
     * @param queryWrapper 包装类
     * @param size 页尺寸
     * @param current 当前页
     * @return PageList<T>
     */
    @SuppressWarnings("unchecked")
    default <T> PageList<T> paginate(QueryWrapper queryWrapper, int size, int current, Class<T> entityClass) {
        long offset = (long)size * (current - 1);
        if(offset < 0) offset = 0L;

        List<Map<String, Object>> list = new ArrayList<>();
        List<Total> totalList = new ArrayList<>();


        List<Map<String, Object>> objList = queryEasy(queryWrapper.limit(offset, (long)size));
        if(objList.get(0) instanceof List<?>){
            list = (List<Map<String, Object>>) objList.get(0);
        }
        if(objList.get(1) instanceof List<?>){
            totalList = (List<Total>) objList.get(1);
        }

        long total =totalList.get(0).getTotal();
        Page page = new Page(total, size, current);
        try {
            return new PageList<>(EntityMapUtil.mapToEntityList(list, entityClass), page);
        }catch (Exception ex){
            throw new RuntimeException("data converted to List<T> failed");
        }
    }

    default RecordList list(QueryWrapper wrapper){
        List<Record> recordList = listByWrapper(wrapper);
        return new RecordList(recordList);
    }

    default Record getSingle(QueryWrapper wrapper) {
        List<Record> recordList = listByWrapper(wrapper);
        if (recordList.size() == 0) return null;
        if (recordList.size() > 1) throw new RuntimeException("查询结果不唯一");
        return recordList.get(0);
    }

    default Record getOne(QueryWrapper wrapper) {
        List<Record> recordList = listByWrapper(wrapper);
        if (recordList.size() == 0) return null;
        return recordList.get(0);
    }

    default long count(QueryWrapper wrapper) {
        return countByWrapper(wrapper);
    }

    default int delete(QueryWrapper wrapper) {
        return deleteByWrapper(wrapper, false);
    }
    default int delete(QueryWrapper wrapper, boolean force) {
        return deleteByWrapper(wrapper, force);
    }

    default int update(Map<String, Object> record, QueryWrapper wrapper) {
        return updateByWrapper(record, wrapper);
    }
}
