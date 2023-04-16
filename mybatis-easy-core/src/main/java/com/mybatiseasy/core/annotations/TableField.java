package com.mybatiseasy.core.annotations;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 标识数据表字段
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableField {
    /**
     * 数据库字段名称，不填则默认为名称转下划线
     */
    @AliasFor("column")
    String value() default "";

    @AliasFor("value")
    String column() default "";

    /**
     * update的时候自动填充默认值
     */
    String updateDefault() default "";

    /**
     * insert的时候自动填充默认值
     */
    String insertDefault() default "";

    /**
     * 是否大字段
     */
    boolean isLarge() default true;

    /**
     * JDBC类型
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * 类型处理
     */
    Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;

    /**
     * 指定小数点后保留的位数
     */
    String numericScale() default "";

    /**
     * 字段描述
     */
    String desc() default "";
}