package com.mybatiseasy.core.base;

import com.mybatiseasy.core.consts.Method;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.paginate.Total;
import com.mybatiseasy.core.provider.SqlProvider;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
     * 根据主键查询一条实例
     * @param id 主键值
     * @return 实体对象
     */
    @SelectProvider(type = SqlProvider.class, method = Method.GET_BY_ID)
    T getById(@Param(MethodParam.PRIMARY_KEY) Serializable id);

    /**
     * 根据组合条件查询一条实例
     * @param condition 查询条件
     * @return T
     */
    @SelectProvider(type = SqlProvider.class, method = Method.GET_BY_CONDITION)
    T getByCondition(@Param(MethodParam.CONDITION) Condition condition);

    /**
     * 根据包装类查询一条实例
     * @param wrapper 查询包装类
     * @return List<T>
     */
    @SelectProvider(type = SqlProvider.class, method = Method.GET_BY_WRAPPER)
    T getByWrapper(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);

    /**
     * 根据组合条件查询实例列表
     * @param condition 查询条件
     * @return List<T>
     */
    @SelectProvider(type = SqlProvider.class, method = Method.LIST_BY_CONDITION)
    List<T> listByCondition(@Param(MethodParam.CONDITION) Condition condition);

    /**
     * 根据包装类查询实例列表
     * @param wrapper 查询包装类
     * @return List<T>
     */
    @SelectProvider(type = SqlProvider.class, method = Method.LIST_BY_WRAPPER)
    List<T> listByWrapper(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);

    /**
     * 根据组合条件统计实例数量
     * @param condition 查询条件
     * @return List<T>
     */
    @SelectProvider(type = SqlProvider.class, method = Method.COUNT_BY_CONDITION)
    Long countByCondition(@Param(MethodParam.CONDITION) Condition condition);

    /**
     * 根据包装类统计实例数量
     * @param wrapper 查询包装类
     * @return List<T>
     */
    @SelectProvider(type = SqlProvider.class, method = Method.COUNT_BY_WRAPPER)
    Long countByWrapper(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);

    /**
     * 根据包装类分页查询
     * @param wrapper 查询包装类
     * @return List<T>
     */
    @SelectProvider(type = SqlProvider.class, method = Method.PAGINATE_EASY)
    List<List<Object>> paginateEasy(@Param(MethodParam.WRAPPER) QueryWrapper wrapper);

}
