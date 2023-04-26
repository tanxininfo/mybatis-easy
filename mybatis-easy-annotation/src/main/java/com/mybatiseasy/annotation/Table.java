package com.mybatiseasy.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 标识数据表
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * 表名称，不填则默认为名称转下划线
     */
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    /**
     * 数据库schema
     */
    String schema() default "";

    /**
     * 表描述
     */
    String desc() default "";
}