/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mybatiseasy.core.utils;

import com.mybatiseasy.core.enums.DbType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class DbTypeUtil {
    /**
     * 数据库连接url
     */
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    private static String url;

    @PostConstruct
    public void setUrl(){ url = this.datasourceUrl; }

    /**
     * 通过DataSource获取数据库类型
     * @param dataSource DataSource
     * @return DbType
     */
    public static DbType getDbType(DataSource dataSource){
        try {
            return getDbType(dataSource.getConnection().getMetaData().getURL());
        }catch (Exception e){
            throw new RuntimeException("数据库连接获取失败");
        }
    }

    /**
     * 数据数据库连接url获取数据库方言
     * @param url 数据库连接
     * @return DbType
     */
    public static DbType getDbType(String url){
        url = url.toLowerCase();
        if(url.contains(":mysql:")) return DbType.MYSQL;
        else if(url.contains(":sqlite:")) return DbType.SQLITE;
        else return DbType.OTHER;
    }

    /**
     * 数据数据库连接url获取数据库方言
     * @return DbType
     */
    public static DbType getDbType(){
        url = url.toLowerCase();
        if(url.contains(":mysql:")) return DbType.MYSQL;
        else if(url.contains(":sqlite:")) return DbType.SQLITE;
        else return DbType.OTHER;
    }
}

