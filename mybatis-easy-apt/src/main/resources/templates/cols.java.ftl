package com.mybatiseasy.core.cols;

import com.mybatiseasy.core.base.Column;

public class UserColumn extends Column {

    public UserColumn(){

    }

    public UserColumn(String table) {
        super(table);
    }

    public UserColumn(String table, String tableAlias) {
        super(table, tableAlias);
    }

    public UserColumn ID() {
        addColumn("`id`");
        return this;
    }

    public UserColumn NAME() {
        addColumn("`name`");
        return this;
    }

    public UserColumn AGE() {
        addColumn("`age`");
        return this;
    }

    public UserColumn SEX() {
        addColumn("`sex`");
        return this;
    }

    public UserColumn PARENT_ID() {
        addColumn("`parent_id`");
        return this;
    }


    public UserColumn ID(String alias) {
        addColumn("`id`", alias);
        return this;
    }

    public UserColumn NAME(String alias) {
        addColumn("`name`", alias);
        return this;
    }

    public UserColumn AGE(String alias) {
        addColumn("`age`", alias);
        return this;
    }

    public UserColumn SEX(String alias) {
        addColumn("`sex`", alias);
        return this;
    }

    public UserColumn PARENT_ID(String alias) {
        addColumn("`parent_id`", alias);
        return this;
    }

    public UserColumn as(String columnAlias) {
        super.columnAlias(columnAlias);
        return this;
    }

    public UserColumn avg(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("AVG");
        return this;
    }

    public UserColumn max(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("MAX");
        return this;
    }

    public UserColumn min(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("MIN");
        return this;
    }

    public UserColumn sum(String columnAlias) {
        super.columnAlias(columnAlias);
        super.setMethod("SUM");
        return this;
    }

    public UserColumn count(String columnAlias) {
        super.setMethod("COUNT");
        super.columnAlias(columnAlias);
        return this;
    }

    public UserColumn avg() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("AVG");
        return this;
    }

    public UserColumn max() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("MAX");
        return this;
    }

    public UserColumn min() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("MIN");
        return this;
    }

    public UserColumn sum() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("SUM");
        return this;
    }

    public UserColumn count() {
        super.columnAlias(this.getColumn().getColumn());
        super.setMethod("COUNT");
        return this;
    }
}
