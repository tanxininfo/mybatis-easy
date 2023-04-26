package com.mybatiseasy.core.keygen;

import com.mybatiseasy.keygen.IKeyGenerator;
import com.mybatiseasy.core.utils.SnowFlakeIdGenerator;

/**
 * 雪花算法
 * @see <a href="https://juejin.cn/post/7082669476658806792"></a>
 */
public class SnowFlakeKeyGenerator implements IKeyGenerator {
    private static final SnowFlakeIdGenerator generator = new SnowFlakeIdGenerator();
    @Override
    public Object generateId() {

        return generator.nextId();
    }
}