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

package com.mybatiseasy.generator;

import com.mybatiseasy.generator.config.*;
import com.mybatiseasy.generator.dialect.DialectFactory;
import com.mybatiseasy.generator.dialect.IDialect;
import com.mybatiseasy.generator.pojo.*;
import com.mybatiseasy.generator.template.FreemarkerTemplate;
import com.mybatiseasy.generator.template.ITemplate;
import com.mybatiseasy.generator.utils.Utils;
import com.mybatiseasy.keygen.IKeyGenerator;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {
    private DataSourceConfig dataSourceConfig;
    private GlobalConfig globalConfig;
    private EntityConfig entityConfig;
    private MapperConfig mapperConfig;
    private ServiceConfig serviceConfig;
    private ServiceImplConfig serviceImplConfig;
    private DtoConfig dtoConfig;
    private ControllerConfig controllerConfig;

    private ITemplate templateEngine;

    private final Map<String, Class<? extends IKeyGenerator>> keyGeneratorMap = new HashMap<>();

    private String versionName = "";
    private String logicDeleteName = "";
    private String tenantIdName = "";


    public Generator dataSourceConfig(DataSourceConfig dataSourceConfig){
         this.dataSourceConfig = dataSourceConfig;
        return this;
    }

    public void templateEngine(ITemplate templateEngine) {
        this.templateEngine = templateEngine;
    }

    public Generator globalConfig(GlobalConfig globalConfig){
        this.globalConfig = globalConfig;
        return this;
    }
    public Generator entityConfig(EntityConfig entityConfig){
        this.entityConfig = entityConfig;
        return this;
    }
    public Generator mapperConfig(MapperConfig mapperConfig){
        this.mapperConfig = mapperConfig;
        return this;
    }
    public Generator serviceConfig(ServiceConfig serviceConfig){
        this.serviceConfig = serviceConfig;
        return this;
    }
    public Generator serviceImplConfig(ServiceImplConfig serviceImplConfig){
        this.serviceImplConfig = serviceImplConfig;
        return this;
    }
    public Generator controllerConfig(ControllerConfig controllerConfig){
        this.controllerConfig = controllerConfig;
        return this;
    }
    public Generator dtoConfig(DtoConfig dtoConfig){
        this.dtoConfig = dtoConfig;
        return this;
    }


    private Connection getConnection() {
        if(Utils.isEmpty(dataSourceConfig.getUrl())) throw new RuntimeException("数据库配置 url 不得为空");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
        } catch(SQLException ex){
            throw new RuntimeException("数据库连接失败:"+ex.getMessage());
        }
        return connection;
    }

    public void generate() {
        this.initConfig();
        this.templateEngine.init(globalConfig,
                entityConfig,
                dtoConfig,
                mapperConfig,
                controllerConfig,
                serviceConfig,
                serviceImplConfig);

        IDialect dialect = DialectFactory.getDialect(dataSourceConfig, globalConfig, entityConfig);
        List<TableInfo> tableList = dialect.getTableList(getConnection());

        if (wantToWrite(TemplateType.SERVICE_IMPL)) {
            this.templateEngine.writeBaseServiceImpl();
        }

        for (TableInfo tableInfo : tableList
        ) {
            if (wantToWrite(TemplateType.ENTITY)) this.templateEngine.writeEntity(tableInfo);
            if (wantToWrite(TemplateType.MAPPER)) this.templateEngine.writeMapper(tableInfo);
            if (wantToWrite(TemplateType.DTO)) this.templateEngine.writeDto(tableInfo);
            if (wantToWrite(TemplateType.CONTROLLER)) this.templateEngine.writeController(tableInfo);
            if (wantToWrite(TemplateType.SERVICE)) this.templateEngine.writeService(tableInfo);
            if (wantToWrite(TemplateType.SERVICE_IMPL)) {this.templateEngine.writeServiceImpl(tableInfo);}
        }
    }

    private void initConfig(){
        if(this.globalConfig== null){
            String rootPath = System.getProperty("user.dir");
            this.globalConfig = new GlobalConfig.Builder(rootPath, "com.mybatiseasy").build();
        }
        if(this.templateEngine == null){
            this.templateEngine = new FreemarkerTemplate();
        }
        if(this.entityConfig == null){
            this.entityConfig = new EntityConfig.Builder("entity", "Entity").build();
        }
        if(this.mapperConfig == null){
            this.mapperConfig = new MapperConfig.Builder("mapper", "Mapper").build();
        }
        if(this.dtoConfig == null){
            this.dtoConfig = new DtoConfig.Builder("dto", "Dto").build();
        }
        if(this.serviceConfig == null){
            this.serviceConfig = new ServiceConfig.Builder("service", "Service").build();
        }
        if(this.serviceImplConfig == null){
            this.serviceImplConfig = new ServiceImplConfig.Builder("service.impl", "ServiceImpl").build();
        }
        if(Utils.isNotEmpty(this.entityConfig.getVersionName())) this.versionName = this.entityConfig.getVersionName();
        if(Utils.isNotEmpty(this.entityConfig.getLogicDeleteName())) this.logicDeleteName = this.entityConfig.getLogicDeleteName();
        if(Utils.isNotEmpty(this.entityConfig.getTenantIdName())) this.tenantIdName = this.entityConfig.getTenantIdName();
    }

    /**
     * 是否需要生成文件
     * @param templateType 类型
     * @return boolean
     */
    private boolean wantToWrite(TemplateType templateType) {
        return globalConfig.getTemplateTypeList().contains(templateType) || globalConfig.getTemplateTypeList().contains(TemplateType.ALL);
    }
 }
