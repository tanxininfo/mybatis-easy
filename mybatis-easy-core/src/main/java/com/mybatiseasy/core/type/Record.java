package com.mybatiseasy.core.type;

import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.utils.BeanMapUtil;
import com.mybatiseasy.core.utils.EntityMapUtil;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import com.mybatiseasy.core.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;

@Slf4j
public class Record extends LinkedHashMap<String, Object> {
    public <T> T toEntity(Class<T> entityClass) {
        try {
            log.info("this={}", ObjectUtil.toJson(this));
            return EntityMapUtil.mapToEntity(this, entityClass);
        }catch (Exception ex){
            log.info("recordToEntity={}", ex.getMessage());
            return null;
        }
    }
}
