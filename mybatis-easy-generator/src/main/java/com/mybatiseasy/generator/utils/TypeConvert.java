package com.mybatiseasy.generator.utils;

import com.mybatiseasy.generator.pojo.JavaDataType;

public class TypeConvert {
    public static JavaDataType fromDbType(String dbType){
        dbType = dbType.toUpperCase();
        if(dbType.contains("BIT(1)")) return JavaDataType.BOOLEAN;
        else if(dbType.contains("TINYINT(1)")) return JavaDataType.BOOLEAN;
        else if(dbType.contains("BIT")) return JavaDataType.BYTE_ARRAY;
        else if(dbType.contains("BOOL")) return JavaDataType.BOOLEAN;
        else if(dbType.contains("SMALLINT")) return JavaDataType.INTEGER;
        else if(dbType.contains("MEDIUMINT")) return JavaDataType.INTEGER;
        else if(dbType.contains("BIGINT")) return JavaDataType.LONG;
        else if(dbType.contains("INT") && isUnsigned(dbType)) return JavaDataType.LONG;
        else if(dbType.contains("INT")) return JavaDataType.INTEGER;
        else if(dbType.contains("FLOAT")) return JavaDataType.FLOAT;
        else if(dbType.contains("DOUBLE")) return JavaDataType.DOUBLE;
        else if(dbType.contains("DECIMAL")) return JavaDataType.BIG_DECIMAL;
        else if(dbType.contains("DATETIME")) return JavaDataType.LOCAL_DATE_TIME;
        else if(dbType.contains("DATE")) return JavaDataType.LOCAL_DATE;
        else if(dbType.contains("TIMESTAMP")) return JavaDataType.TIMESTAMP;
        else if(dbType.contains("TIME")) return JavaDataType.TIME;
        else if(dbType.contains("YEAR")) return JavaDataType.SHORT;
        else if(dbType.contains("CHAR")) return JavaDataType.STRING;
        else if(dbType.contains("VARCHAR")) return JavaDataType.STRING;
        else if(dbType.contains("BINARY")) return JavaDataType.BYTE_ARRAY;
        else if(dbType.contains("VARBINARY")) return JavaDataType.BYTE_ARRAY;
        else if(dbType.contains("TINYBLOB")) return JavaDataType.BYTE_ARRAY;
        else if(dbType.contains("BYTE_ARRAY")) return JavaDataType.STRING;
        else if(dbType.contains("BLOB")) return JavaDataType.BYTE_ARRAY;
        else if(dbType.contains("TEXT")) return JavaDataType.STRING;
        else if(dbType.contains("MEDIUMBLOB")) return JavaDataType.BYTE_ARRAY;
        else if(dbType.contains("MEDIUMTEXT")) return JavaDataType.STRING;
        else if(dbType.contains("LONGBLOB")) return JavaDataType.BYTE_ARRAY;
        else if(dbType.contains("LONGTEXT")) return JavaDataType.STRING;
        else if(dbType.contains("ENUM")) return JavaDataType.STRING;
        else  return JavaDataType.STRING;
    }

    private static boolean isUnsigned(String dbType){
        return dbType.contains("unsigned");
    }
}
