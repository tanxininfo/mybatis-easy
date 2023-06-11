package com.mybatiseasy.core.type;

import com.mybatiseasy.core.utils.EntityMapUtil;
import com.mybatiseasy.core.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RecordList {
    private final List<Record> recordList;

    public RecordList(List<Record> list){
        this.recordList = list;
    }


    public <T> List<T> toEntityList(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        for (Record record:recordList
             ) {
            list.add(record.toEntity(entityClass));
        }
        return list;
    }


    public <T> List<T> toBeanList(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        for (Record record:recordList
        ) {
            list.add(record.toBean(entityClass));
        }
        return list;
    }
}
