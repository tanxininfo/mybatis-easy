package com.mybatiseasy.core.mapper;

import com.mybatiseasy.core.consts.Method;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.provider.SqlProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * 所有Entity对应Mapp氏  er通过继承该接口取得CRUD功能。
 */
public interface IMapper<T> {
    /**
     * 插入一个实体
     * @param entity 实例
     * @return 插入行数
     */
    @InsertProvider(type = SqlProvider.class, method = Method.INSERT)
    int insert(@Param(MethodParam.ENTITY) T entity);


    /**
     * 批量插入实体
     * @param entityList 实例列表
     * @return 插入行数
     */
    @InsertProvider(type = SqlProvider.class, method = Method.INSERT_BATCH)
    int insertBatch(@Param(MethodParam.ENTITY_LIST) List<T> entityList);

    /**
     * 根据 主键删除行
     * @param id 主键值
     * @return
     */
    @DeleteProvider(type = SqlProvider.class, method = Method.DELETE_BY_ID)
    int deleteById(@Param(MethodParam.PRIMARY_KEY) Serializable id);

    /**
     * 根据 主键删除行
     * @param id 主键值
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method = Method.GET_BY_ID)
    T getById(@Param(MethodParam.PRIMARY_KEY) Serializable id);
}
