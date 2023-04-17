package com.mybatiseasy.core.sqlbuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryWrapper implements Serializable {
    private List<Condition> whereList= new ArrayList<>();
    private List<String> selectList = new ArrayList<>();

    public static QueryWrapper create(){
        return new QueryWrapper();
    }

    public QueryWrapper where(Condition condition){
        whereList.add(condition);
        return this;
    }

    /**
     * 查询的数据字段列表
     * @param columns 数据列
     * @return QueryWrapper
     */
    public QueryWrapper select(Condition ...columns){
        for (Condition condition: columns
             ) {
            String column = condition.getColumn();
            if(!selectList.contains(column)) selectList.add(condition.getColumn());
        }
        return this;
    }

    public String generateSql(){
        return "";
    }
}
