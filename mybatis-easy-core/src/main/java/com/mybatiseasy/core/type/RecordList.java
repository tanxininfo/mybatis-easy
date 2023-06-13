package com.mybatiseasy.core.type;

import com.mybatiseasy.core.utils.EntityMapUtil;
import com.mybatiseasy.core.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RecordList  implements Serializable {
    private List<Record> recordList;

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

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
