/*
 *
 *  *
 *  *  * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package com.mybatiseasy.generator.dialect;

import com.mybatiseasy.generator.config.DataSourceConfig;
import com.mybatiseasy.generator.config.EntityConfig;
import com.mybatiseasy.generator.config.GlobalConfig;
import com.mybatiseasy.generator.utils.DbTypeUtil;


public class DialectFactory {

    public static IDialect getDialect(DataSourceConfig dataSourceConfig, GlobalConfig globalConfig, EntityConfig entityConfig){
        DbType dbType = DbTypeUtil.getDbType(dataSourceConfig.getUrl());
        if (dbType == DbType.SQLITE) {
            return new SqliteDialect(dataSourceConfig, globalConfig, entityConfig);
        } else {
            return new MySQLDialect(dataSourceConfig, globalConfig, entityConfig);
        }
    }
}