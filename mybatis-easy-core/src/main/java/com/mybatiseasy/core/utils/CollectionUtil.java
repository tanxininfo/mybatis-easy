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
