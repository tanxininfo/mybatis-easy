/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

package com.mybatiseasy.core.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {

    /**
     * 判断 Collection 是否为空
     *
     * @param collection Collection<?>
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断 Collection 是否不为空
     *
     * @param collection Collection<?>
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }


    /**
     * 对象转为列表
     * @param param 对象
     * @return Collection<?>
     */
    public static Collection<?> collectionize(Object param) {
        if (param instanceof Collection) {
            return (Collection<?>) param;
        }
        if (param instanceof Object[]) {
            return Arrays.asList((Object[]) param);
        } else {
            return List.of(param);
        }
    }


}
