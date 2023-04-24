package com.mybatiseasy.core.annotations;

import com.mybatiseasy.core.keygen.IKeyGenerator;
import com.mybatiseasy.core.enums.TableIdType;
import com.mybatiseasy.core.keygen.NoKeyGenerator;
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
     * idType为SEQUENCE 时应设此值
     * @return String
     */
    String sequence() default "";


    /**
     * 主键自增类型
     */
    TableIdType idType() default TableIdType.AUTO;

    Class<? extends IKeyGenerator> keyGenerator() default NoKeyGenerator.class;

}