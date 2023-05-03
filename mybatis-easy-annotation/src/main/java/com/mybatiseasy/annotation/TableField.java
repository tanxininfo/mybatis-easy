/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mybatiseasy.annotation;

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
     * update的时候自动填充默认值,字符串请用''包裹
     */
    String update() default "";

    /**
     * insert的时候自动填充默认值,字符串请用''包裹
     */
    String insert() default "";

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