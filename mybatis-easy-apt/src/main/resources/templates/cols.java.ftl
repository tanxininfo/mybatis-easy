package com.mybatiseasy.core.cols;

import com.mybatiseasy.core.base.Column;

public class ${colClassName} extends Column {

    public ${colClassName}(){

    }

    public ${colClassName}(String table) {
        super(table);
    }

    public ${colClassName}(String table, String tableAlias) {
        super(table, tableAlias);
    }

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public ${colClassName} ${column.capitalName}() {
        addColumn("`${column.column}`");
        return this;
    }

</#list>
<#------------  END 字段循环遍历  ---------->


<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public ${colClassName} ${column.capitalName}(String alias) {
        addColumn("`${column.column}`", alias);
        return this;
    }

</#list>
<#------------  END 字段循环遍历  ---------->


    public ${colClassName} as(String columnAlias) {
        super.columnAlias(columnAlias);
        return this;
    }

    public ${colClassName} avg(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("AVG");
        return this;
    }

    public ${colClassName} max(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("MAX");
        return this;
    }

    public ${colClassName} min(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("MIN");
        return this;
    }

    public ${colClassName} sum(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("SUM");
        return this;
    }

    public ${colClassName} count(String columnAlias) {
        super.setMethod("COUNT");
        super.columnAlias(columnAlias);
        return this;
    }

    public ${colClassName} avg() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("AVG");
        return this;
    }

    public ${colClassName} max() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("MAX");
        return this;
    }

    public ${colClassName} min() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("MIN");
        return this;
    }

    public ${colClassName} sum() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("SUM");
        return this;
    }

    public ${colClassName} count() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("COUNT");
        return this;
    }
}
