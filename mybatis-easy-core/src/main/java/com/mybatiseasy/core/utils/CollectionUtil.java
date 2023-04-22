package com.mybatiseasy.core.utils;

import java.util.Collection;

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


}
