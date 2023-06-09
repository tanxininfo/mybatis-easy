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