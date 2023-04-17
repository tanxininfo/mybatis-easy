package com.mybatiseasy.core.table;
import com.mybatiseasy.core.sqlbuilder.Condition;

public class _ORDER {
        public static String _name = "`order`";
        public static Condition ID = new Condition(_name, "`id`");
        public static Condition CREATE_TIME = new Condition(_name, "`create_time`");
    }
