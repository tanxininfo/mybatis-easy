package com.mybatiseasy.core.sqlbuilder;

import com.mybatiseasy.core.consts.Sql;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Table {
    private String name;
    private String as;

    public Table(String name){
        this.name = name;
        this.as = "";
    }

    public Table(String name, String as) {
        this.name = name;
        this.as = as;
    }
}
