package com.mybatiseasy.core.type;

import com.mybatiseasy.core.base.Column;
import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.session.EntityMap;
import com.mybatiseasy.core.session.EntityMapKids;
import com.mybatiseasy.core.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;


public class Record extends LinkedHashMap<String, Object> implements Serializable {
    public <T> T toEntity(Class<T> entityClass) {
        try {
            return EntityMapUtil.mapToEntity(this, entityClass);
        }catch (Exception ex){
            throw new RuntimeException("record converted to entity failed:" +  ex.getMessage());
        }
    }

    public <T> T toBean(Class<T> entityClass) {
        try {
            return BeanMapUtil.mapToBean(this, entityClass);
        }catch (Exception ex){
            throw new RuntimeException("record converted to bean failed:" +  ex.getMessage());
        }
    }

    public void set(String key, Object value){
        this.put(key, value);
    }

    public void set(Column key, Object value){
        this.put(SqlUtil.removeBackquote(key.getColumn().getColumn()), value);
    }

}
