package com.mybatiseasy.core.base;

import lombok.Data;

@Data
public class ColumnData {
    private String tableAlias;

    private String table;

    private String column;

    /**
     * 聚合查询名称
     */
    private String method;

    private String columnAlias;
}
