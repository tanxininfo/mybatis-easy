package com.mybatiseasy.core.table;
import com.mybatiseasy.core.cols.UserColumn;
import com.mybatiseasy.core.base.Table;

public class _USER extends Table {
    public static UserColumn ID() {
        return new UserColumn().ID();
    }

    public static UserColumn NAME() {
        return new UserColumn().NAME();
    }

    public static UserColumn AGE() {
        return new UserColumn().AGE();
    }

    public static UserColumn SEX() {
        return new UserColumn().SEX();
    }

    public static UserColumn PARENT_ID() {
        return new UserColumn().PARENT_ID();
    }

    public static UserColumn ID(String alias) {
        return new UserColumn().ID(alias);
    }

    public static UserColumn NAME(String alias) {
        return new UserColumn().NAME(alias);
    }

    public static UserColumn AGE(String alias) {
        return new UserColumn().AGE(alias);
    }

    public static UserColumn SEX(String alias) {
        return new UserColumn().SEX(alias);
    }

    public static UserColumn PARENT_ID(String alias) {
        return new UserColumn().PARENT_ID(alias);
    }

    public static UserColumn as(String tableAlias) {
        return new UserColumn("`user`", tableAlias);
    }

    public static UserColumn nm() {
        return new UserColumn("`user`", "");
    }
}
