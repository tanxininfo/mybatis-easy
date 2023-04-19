package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Table {
    private final String name;
    private String alias = "";

    public Table(String name){
        this.name = name;
    }

    private Table(String name, String alias){
        this.name = name;
        this.alias = alias;
    }
    public Table as(String alias){
        return new Table(name, alias);
    }

    public String getName(){
        return this.name;
    }

    public String getAlias(){
        return this.alias;
    }
}
