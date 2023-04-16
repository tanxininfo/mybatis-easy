package com.mybatiseasy.core.provider;

import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.session.MyConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;

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
        return "SELECT * FROM "+ entityMap.getName()+" where id="+map.get(MethodParam.PRIMARY_KEY).toString();
    }

    /**
     * 根据组合条件查询一个实体
     * @param map
     * @param context
     * @return
     */
    public static String getByConditions(Map map, ProviderContext context) {
        EntityMap entityMap = EntityMapKids.getEntityMapByContext(context);
        log.info("map={}",map);
        return "SELECT * FROM "+ entityMap.getName()+" where id="+map.get(MethodParam.PRIMARY_KEY).toString();
    }

}
