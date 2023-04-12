package com.mybatiseasy.core.mapper;

import com.mybatiseasy.core.consts.MethodConst;
import com.mybatiseasy.core.consts.ParamConst;
import com.mybatiseasy.core.provider.SqlProvider;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 所有Entity对应Mapper通过继承该接口取得CRUD功能。
 */
public interface IMapper<T> {
    /**
     * 插入一个实体
     * @param entity 实例
     * @return 插入行数
     */
    @InsertProvider(type = SqlProvider.class, method = MethodConst.INSERT)
    int insert(@Param(ParamConst.ENTITY) T entity);


    /**
     * 批量插入实体
     * @param entityList 实例列表
     * @return 插入行数
     */
    @InsertProvider(type = SqlProvider.class, method = MethodConst.INSERT_BATCH)
    int insertBatch(@Param(ParamConst.ENTITY_LIST) List<T> entityList);

    /**
     * 根据 主键删除行
     * @param id 主键值
     * @return
     */
    @DeleteProvider(type = SqlProvider.class, method = MethodConst.DELETE_BY_ID)
    int deleteById(@Param(ParamConst.PRIMARY_KEY) Serializable id);

    /**
     * 根据 主键删除行
     * @param id 主键值
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method = MethodConst.GET_BY_ID)
    T getById(@Param(ParamConst.PRIMARY_KEY) Serializable id);
}
