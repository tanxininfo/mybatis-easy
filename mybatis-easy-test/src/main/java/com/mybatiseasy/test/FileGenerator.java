/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

package com.mybatiseasy.test;

import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.generator.Generator;
import com.mybatiseasy.generator.config.*;
import com.mybatiseasy.generator.pojo.ColumnAutoSet;
import com.mybatiseasy.keygen.NoKeyGenerator;

import java.util.Map;

public class FileGenerator {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:mybatis-easy-test/sqlite/test.db";
        String username = "";
        String password = "";

        String baseDir =  System.getProperty("user.dir") + "\\mybatis-easy-test\\src\\main\\java";

        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(url, username, password).build();

        GlobalConfig globalConfig = new GlobalConfig.Builder(baseDir, "com.mybatiseasy.test.out")
                .templateType(TemplateType.ALL)

                .idType(TableIdType.CUSTOM)
                .keyGenerator(NoKeyGenerator.class)
                .build();

        EntityConfig entityConfig = new EntityConfig.Builder("entity", "")
                .override(true)
                .swagger(true)
                .enableLombok(false)
                .chain(true)
                .prefix("tx_")
                .versionName("version")
                .tenantIdName("tenantId")
                .logicDeleteName("deleted")
                .logicDeleteValue("null", "0")
                .columnAutoSet(new ColumnAutoSet().setName("createTime").setInsert("DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s')"))
                .columnAutoSet(new ColumnAutoSet().setName("updateTime").setInsert("DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s')").setUpdate("DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s')"))
                .build();

        MapperConfig mapperConfig = new MapperConfig.Builder("mapper", "Mapper")
                .override(false)
                .build();

        DtoConfig dtoConfig = new DtoConfig.Builder("dto", "Dto")
                .override(true)
                .swagger(true)
                .build();

        ControllerConfig controllerConfig = new ControllerConfig.Builder("controller", "Controller")
                .override(true)
                .build();

        ServiceConfig serviceConfig = new ServiceConfig.Builder("service", "Service")
                .override(true)
                .build();

        ServiceImplConfig serviceImplConfig = new ServiceImplConfig.Builder("service.impl", "ServiceImpl")
                .override(true)
                .build();

        new Generator()
                .dataSourceConfig(dataSourceConfig)
                .globalConfig(globalConfig)
                .entityConfig(entityConfig)
                .mapperConfig(mapperConfig)
                .controllerConfig(controllerConfig)
                .dtoConfig(dtoConfig)
                .serviceConfig(serviceConfig)
                .serviceImplConfig(serviceImplConfig)
                .generate();
    }
}
