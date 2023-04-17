package com.mybatiseasy.core.provider;

import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;

import javax.management.Query;
import java.util.Map;

@Slf4j
public class SqlProvider {
    private SqlProvider() {
    }

    /**
     * 插入一个实体
     * @param map
     * @param context
     * @return
     */
    public static String insert(Map map, ProviderContext context) {
      log.info("map={}", map);
       return "insert into u_user(id, username) values(1234, 'hahaha')";
    }

    /**
     * 插入一个实体
     * @param map
     * @param context
     * @return
     */
    public static String insertBatch(Map map, ProviderContext context) {
        log.info("map={}", map);
        return "insert into u_user(id, username) values(1234, 'hahaha')";
    }

    /**
     * 插入一个实体
     * @param map
     * @param context
     * @return
     */
    public static String deleteById(Map map, ProviderContext context) {
        log.info("map={}", map);
        return "insert into u_user(id, username) values(1234, 'hahaha')";
    }

    /**
     * 根据Id查询一个实体
     * @param map
     * @param context
     * @return
     */
    public static String getById(Map map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        return "SELECT * FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                "where" + Sql.SPACE + entityMap.getPrimary() + "=" + Sql.SPACE +
                map.get(MethodParam.PRIMARY_KEY).toString();
    }

    /**
     * 根据组合条件查询一个实体
     * @param map 条件
     * @param context  上下文
     * @return String
     */
    public static String getByCondition(Map map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);

        return "SELECT * FROM" + Sql.SPACE +
                entityMap.getName() + Sql.SPACE +
                " WHERE " + Sql.SPACE +
                condition.getSql();
    }

    /**
     * 根据组合条件查询实体列表
     * @param map 条件
     * @param context  上下文
     * @return String
     */
    public static String listByCondition(Map map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        Condition condition = (Condition) map.get(MethodParam.CONDITION);
        return "SELECT * FROM" + Sql.SPACE +
                entityMap.getName() +
                Sql.SPACE + "WHERE" + condition.getSql();
    }

    /**
     * 根据组合条件查询实体列表
     * @param map 条件
     * @param context  上下文
     * @return String
     */
    public static String listByWrapper(Map map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        QueryWrapper wrapper = (QueryWrapper) map.get(MethodParam.WRAPPER);
        return wrapper.generateSql();
    }
}
