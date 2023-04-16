package com.mybatiseasy.core.provider;

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
     * 插入一个实体
     * @param map
     * @param context
     * @return
     */
    public static String getById(Map map, ProviderContext context) {

        Class<?> mapperClass = context.getMapperType();
        log.info("mapperClass={},{}", mapperClass.getName(), context.getMapperMethod().getName(), mapperClass.getGenericInterfaces()[0]);
        return "select * from `order` where id=2301010015420437";
    }

}
