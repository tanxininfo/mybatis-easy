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

package com.mybatiseasy.test.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatiseasy.annotation.EnumValue;

import java.util.HashMap;
import java.util.Map;

public enum SexEnum {

    PRIMARY(1, "男"), SECONDARY(2, "女"), THIRD(0, "未知");

    SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @EnumValue
    private final int code;
    private final String desc;

    @JsonValue
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("code",getCode());
        map.put("desc",getDesc());
        return map;
    }
}