package com.mybatiseasy.core.sqlbuilder;

import java.io.Serializable;

public class QueryWrapper implements Serializable {
    public QueryWrapper eq(Object val){
        return this;
    }

    public QueryWrapper gt(Object val){
        return this;
    }

    public QueryWrapper and(Object val){
        return this;
    }
}
