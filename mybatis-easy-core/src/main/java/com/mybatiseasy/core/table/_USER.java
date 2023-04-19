package com.mybatiseasy.core.table;
import com.mybatiseasy.core.sqlbuilder.Column;

public class _USER {
    public static String _name = "`user`";
    public static Column ID = new Column(_name, "`id`");
    public static Column NAME = new Column(_name, "`name`");
    public static Column AGE = new Column(_name, "`age`");
    public static Column SEX = new Column(_name, "`sex`");
    public static Column PARENT_ID = new Column(_name, "`parent_id`");
}
