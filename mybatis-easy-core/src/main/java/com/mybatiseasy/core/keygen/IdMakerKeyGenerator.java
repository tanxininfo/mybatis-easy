package com.mybatiseasy.core.keygen;

import com.mybatiseasy.core.utils.IdMakerUtil;

/**
 * 一个轻量算法
 * 时分秒+4位随机
 */
public class IdMakerKeyGenerator implements IKeyGenerator {

    @Override
    public Object generateId() {
        return IdMakerUtil.uniqueId(4).id();
    }
}