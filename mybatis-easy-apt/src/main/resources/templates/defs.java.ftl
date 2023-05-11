package com.mybatiseasy.core.defs;

import com.mybatiseasy.core.base.Column;

public class ${defClassName} extends Column {

    public ${defClassName}(){

    }

    public ${defClassName}(String table) {
        super(table);
    }

    public ${defClassName}(String table, String tableAlias) {
        super(table, tableAlias);
    }

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public ${defClassName} ${column.capitalName}() {
        addColumn("`${column.column}`");
        return this;
    }

</#list>
<#------------  END 字段循环遍历  ---------->


<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public ${defClassName} ${column.capitalName}(String alias) {
        addColumn("`${column.column}`", alias);
        return this;
    }

</#list>
<#------------  END 字段循环遍历  ---------->


    public ${defClassName} as(String columnAlias) {
        super.columnAlias(columnAlias);
        return this;
    }

    public ${defClassName} avg(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("AVG");
        return this;
    }

    public ${defClassName} max(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("MAX");
        return this;
    }

    public ${defClassName} min(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("MIN");
        return this;
    }

    public ${defClassName} sum(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("SUM");
        return this;
    }

    public ${defClassName} count(String columnAlias) {
        super.setMethod("COUNT");
        super.columnAlias(columnAlias);
        return this;
    }

    public ${defClassName} avg() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("AVG");
        return this;
    }

    public ${defClassName} max() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("MAX");
        return this;
    }

    public ${defClassName} min() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("MIN");
        return this;
    }

    public ${defClassName} sum() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("SUM");
        return this;
    }

    public ${defClassName} count() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("COUNT");
        return this;
    }
}
