package com.mybatiseasy.core.annotations;

import com.mybatiseasy.core.enums.TableIdType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 标识表的主键
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableId {
    /**
     * 数据库字段名称, 不填则默认为名称转下划线
     */
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    /**
     * 主键自增类型
     */
    TableIdType idType() default TableIdType.AUTO;

    /**
     * JDBC类型
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * 类型处理
     */
    Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;

    /**
     * 字段描述
     */
    String desc() default "";
}