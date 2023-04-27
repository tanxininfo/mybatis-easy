package com.mybatiseasy.generator;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Slf4j
public class Entry {
    private String url = "jdbc:mysql://dev.tanxin.info:3306/tanxin-db?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true";
    private String username = "tanxin";
    private String password = "asEedio234#r3";

    public Entry datasource(){
        try {
            Connection connection =
                    DriverManager.getConnection(url, username, password);


            Statement statement = connection.createStatement();
            statement.execute("select * from user");

        }catch (Exception ex){
            log.error("出错啦" + ex.getMessage());
        }
        return this;
    }
}
