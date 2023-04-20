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
}
