package com.mybatiseasy.core.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BeanMapUtil {

    /**
     * 对象转Map
     * @param object 对象
     * @return Map
     * @throws Exception ex
     */
    public static <T> Map<String, Object> beanToMap(T object) throws Exception {
        Map<String, Object> map  = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }
        return map;
    }

    /**
     * map转对象
     * @param map Map
     * @param beanClass 对象类
     * @return 对象
     * @param <T> 对象类型
     * @throws Exception ex
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) throws Exception {
        T object = beanClass.getDeclaredConstructor().newInstance();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            String snakeName = StringUtil.camelToSnake(field.getName());
            if (map.containsKey(snakeName)) {
                field.set(object, ConversionUtil.convertValue(map.get(snakeName), field.getType()));
            }
        }
        return object;
    }

    public static <T> List<Map<String, Object>> beanListToMapList(List<T> objList) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map;
            T bean;
            for (T t : objList) {
                bean = t;
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    public static <T> List<T> mapListToBeanList(List<Map<String, Object>> maps, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map;
            for (Map<String, Object> stringMap : maps) {
                map = stringMap;
                T bean = mapToBean(map, clazz);
                list.add(bean);
            }
        }
        return list;
    }
}
