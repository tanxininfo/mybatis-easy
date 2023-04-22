package com.mybatiseasy.core.utils;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象和map的互相转换
 */
@Slf4j
public class ArrayUtil {

    public static boolean isEmpty(Object[] array){
        return (array== null) || array.length<=0;
    }

    public static boolean isNotEmpty(Object[] array){
        return !isEmpty(array);
    }
}
