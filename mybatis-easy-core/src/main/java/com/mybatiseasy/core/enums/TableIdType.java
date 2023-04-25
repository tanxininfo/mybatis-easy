/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mybatiseasy.core.enums;

import lombok.Getter;


/**
 * 数据表主键自增类型
 */
@Getter
public enum TableIdType {
    /**
     * 未设置自增，需手动指定
     */
    NONE,

    /**
     * 数据库ID自增,数据表要设置为自增
     */
    AUTO,

    /**
     * UUID ,默认为UUID.replace("-","")
     */
    UUID,

    /**
     * 雪花算法
     */
    SNOW_FLAKE,

    /**
     * 一个轻量ID,时分秒+随机
     */
    ID_MAKER,

    /**
     * SEQUENCE
     */
    SEQUENCE,

    /**
     * 用户自定义
     */
    CUSTOM


}
