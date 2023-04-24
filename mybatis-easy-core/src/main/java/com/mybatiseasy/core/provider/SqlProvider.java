package com.mybatiseasy.core.provider;

import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import com.mybatiseasy.core.utils.SqlBuilderUtil;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
public class SqlProvider {
    private SqlProvider() {
    }

    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String insert(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        return SqlBuilderUtil.getInsertSql(map, entityMap);
    }

    /**
     * 插入一组记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String insertBatch(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        return SqlBuilderUtil.getInsertBatchSql(map, entityMap);
    }


    /**
     * 删除一条记录
     *
     * @param map
     * @param context
     * @return
     */
    public static String deleteById(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Assert.notNull(entityMap.getPrimary(), "实体类未标注TableId");

        return "DELETE FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                "where" + Sql.SPACE + entityMap.getPrimary() + "=" + Sql.SPACE +
                "#{" + MethodParam.PRIMARY_KEY +"}";
    }

    /**
     * 删除多条记录, 根据Condition
     *
     * @param map 参数
     * @param context 上下文
     * @return 影响行数
     */
    public static String deleteByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);

        return "DELETE FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                getConditionSql(condition);
    }

    /**
     * 根据包装类删除多条记录
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String deleteByWrapper(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getValueMap());

        SqlUtil.initDeleteWrapper(wrapper, entityMap.getName());
         Assert.isTrue(!wrapper.hasWhere() && map.get("force")==null, "删除条件不得为空");

        return wrapper.getSql();
    }

    /**
     * 根据Id查询一个实体
     *
     * @param map 参数map
     * @param context 上下文
     * @return String
     */
    public static String getById(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Assert.notNull(entityMap.getPrimary(), "实体类未标注TableId");

        return "SELECT * FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                "where" + Sql.SPACE + entityMap.getPrimary() + "=" + Sql.SPACE +
                "#{" + MethodParam.PRIMARY_KEY +"}";
    }

    /**
     * 拼接Condition条件
     * @param condition  条件组合
     * @return String
     */
    private static String getConditionSql(Condition condition) {
        return condition.getSql().isEmpty() ? "" : "WHERE" + Sql.SPACE + condition.getSql();
    }

    /**
     * 根据组合条件查询一个实体
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String getByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);

        return "SELECT * FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                getConditionSql(condition);
    }

    /**
     * 根据包装类查询一条实体
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String getByWrapper(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getValueMap());

        SqlUtil.initWrapper(wrapper, entityMap.getName());

        return wrapper.getSql();
    }

    /**
     * 根据组合条件查询实体列表
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String listByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getValueMap());

        return "SELECT * FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                getConditionSql(condition);

    }

    /**
     * 根据包装类查询实体列表
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String listByWrapper(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getValueMap());

        SqlUtil.initWrapper(wrapper, entityMap.getName());
        return wrapper.getSql();
    }

    /**
     * 根据组合条件统计记录数
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String countByCondition(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        map.putAll(condition.getValueMap());

        return "SELECT count(*) FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                getConditionSql(condition);
    }

    /**
     * 根据组合条件统计记录数
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String countByWrapper(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getValueMap());

        SqlUtil.initWrapper(wrapper, entityMap.getName());
        return wrapper.getSql();
    }

    /**
     * 根据包装类分页查询
     *
     * @param map     条件
     * @param context 上下文
     * @return String
     */
    public static String queryEasy(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        map.putAll(wrapper.getValueMap());

        SqlUtil.initWrapper(wrapper, entityMap.getName());

        return wrapper.getSqlPaginate();
    }
}
