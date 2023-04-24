package com.mybatiseasy.core.keygen;

public interface IKeyGenerator {
    /**
     * 生成Id的方法
     * @param entity 实体映射对象
     * @return
     */
    Object generateId(Object entity);
}
