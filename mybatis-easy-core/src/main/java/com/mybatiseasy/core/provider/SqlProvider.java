package com.mybatiseasy.core.provider;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.springframework.util.Assert;

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

        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertParts(map, entityMap);

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.insertInto(entityMap.getName());
        wrapper.columns(builder.getInsertSymbolList().toArray());
        wrapper.valuesList(builder.getInsertValuesList());

        return wrapper.getSql();
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
        SqlBuilder builder = new SqlBuilder();
        builder.generateInsertBatchParts(map, entityMap);

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.insertInto(entityMap.getName());
        wrapper.columns(builder.getInsertSymbolList().toArray());
        wrapper.valuesList(builder.getInsertValuesList());

        return wrapper.getSql();
    }

    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String updateById(Map<String, Object> map, ProviderContext context) {
//        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
//        return SqlBuilderUtil.getUpdateSql(map, entityMap);
        return "";
    }

    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String updateByCondition(Map<String, Object> map, ProviderContext context) {
//        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
//        return SqlBuilderUtil.getUpdateSql(map, entityMap);
        return "";
    }

    /**
     * 插入一条记录
     *
     * @param map 参数
     * @param context  上下文
     * @return String
     */
    public static String updateByWrapper(Map<String, Object> map, ProviderContext context) {
//        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
//        return SqlBuilderUtil.getUpdateSql(map, entityMap);
        return "";
    }

    /**
     * 删除一条记录
     *
     * @param map 条件
     * @param context  上下文
     * @return
     */
    public static String deleteById(Map<String, Object> map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Assert.notNull(entityMap.getPrimary(), "实体类未标注TableId");

        QueryWrapper wrapper = new QueryWrapper();
        SqlUtil.initDeleteWrapper(wrapper, entityMap.getName());
        String where = entityMap.getPrimary().getColumn() + "=" + Sql.SPACE +
                "#{" + MethodParam.PRIMARY_KEY +"}";;
        wrapper.where(where);

        return wrapper.getSql();
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
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = new QueryWrapper();
        SqlUtil.initDeleteWrapper(wrapper, entityMap.getName());
        wrapper.where(condition);

        return wrapper.getSql();
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
        map.putAll(wrapper.getParameterMap());

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

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("*");
        wrapper.from(new Column(entityMap.getName()));
        String where = entityMap.getPrimary().getColumn() + "=" + Sql.SPACE +
                "#{" + MethodParam.PRIMARY_KEY +"}";;
        wrapper.where(where);

        return wrapper.getSql();
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

        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("*");
        wrapper.from(new Column(entityMap.getName()));
        wrapper.where(condition);

        return wrapper.getSql();
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
        map.putAll(wrapper.getParameterMap());
        log.info("wrapper.getParameterMap()={}", wrapper.getParameterMap());
        SqlUtil.initSelectWrapper(wrapper, entityMap.getName());

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
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("*");
        wrapper.from(new Column(entityMap.getName()));
        wrapper.where(condition);

        return wrapper.getSql();

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
        map.putAll(wrapper.getParameterMap());

        SqlUtil.initSelectWrapper(wrapper, entityMap.getName());
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
        map.putAll(condition.getParameterMap());

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("count(*)");
        wrapper.from(new Column(entityMap.getName()));
        wrapper.where(condition);

        return wrapper.getSql();
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
        map.putAll(wrapper.getParameterMap());

        SqlUtil.initSelectWrapper(wrapper, entityMap.getName());
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
        map.putAll(wrapper.getParameterMap());

        SqlUtil.initSelectWrapper(wrapper, entityMap.getName());

        return wrapper.getSqlPaginate();
    }
}
