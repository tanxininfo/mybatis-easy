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

package com.mybatiseasy.generator.template;

import com.mybatiseasy.generator.config.*;
import com.mybatiseasy.generator.pojo.TableInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerTemplate implements ITemplate{

    private Template entityTemplate;
    private Template mapperTemplate;
    private Template dtoTemplate;
    private Template serviceTemplate;
    private Template serviceImplTemplate;
    private Template controllerTemplate;

    private GlobalConfig globalConfig;
    private EntityConfig entityConfig;
    private MapperConfig mapperConfig;
    private ServiceConfig serviceConfig;
    private ServiceImplConfig serviceImplConfig;
    private DtoConfig dtoConfig;
    private ControllerConfig controllerConfig;

    public FreemarkerTemplate() {
    }

    public void init(GlobalConfig globalConfig,
                      EntityConfig entityConfig,
                      DtoConfig dtoConfig,
                      MapperConfig mapperConfig,
                      ControllerConfig controllerConfig,
                      ServiceConfig serviceConfig,
                      ServiceImplConfig serviceImplConfig
    ) {
        this.globalConfig = globalConfig;
        this.entityConfig  = entityConfig;
        this.dtoConfig = dtoConfig;
        this.controllerConfig = controllerConfig;
        this.mapperConfig = mapperConfig;
        this.serviceConfig = serviceConfig;
        this.serviceImplConfig = serviceImplConfig;

        try {
            Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

            config.setClassForTemplateLoading(getClass(), "/templates/freemarker");
            entityTemplate = config.getTemplate("entity.java.ftl");
            mapperTemplate = config.getTemplate("mapper.java.ftl");
            dtoTemplate = config.getTemplate("dto.java.ftl");
            serviceTemplate = config.getTemplate("service.java.ftl");
            serviceImplTemplate = config.getTemplate("serviceImpl.java.ftl");
            controllerTemplate = config.getTemplate("controller.java.ftl");

            config.setDefaultEncoding("utf-8");
        } catch (Exception ex) {
            throw new RuntimeException("模板初始化失败" + ex.getMessage());
        }
    }

    private String getFilePath(String packageName, String suffix, String tableName){
        String path = globalConfig.getBaseDir()
                + File.separator
                + globalConfig.getPackageName().replace(".", File.separator)
                + File.separator
                + packageName.replace(".", File.separator);

        File file = new File(path);
        if (!file.exists()) file.mkdirs();

        return path
                +File.separator
                + StringUtils.capitalize(tableName)
                + suffix
                + ".java";
    }

    public void writeEntity(TableInfo tableInfo) {
        String filePath = "";
        try {
            filePath = getFilePath(entityConfig.getPackageName(), entityConfig.getSuffix(), tableInfo.getName());
            File file = new File(filePath);
            if (!entityConfig.isOverride() && file.exists()) return;

            Writer out = new FileWriter(file);
            Map<String, Object> paramsMap =new HashMap<>();
            paramsMap.put("global", globalConfig);
            paramsMap.put("entity", entityConfig);
            paramsMap.put("table", tableInfo);

            entityTemplate.process(paramsMap, out);
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException("文件生成失败["+ filePath +"]" + ex.getMessage());
        }
    }


    public void writeDto(TableInfo tableInfo) {
        String filePath = "";
        try {
            filePath = getFilePath(dtoConfig.getPackageName(), dtoConfig.getSuffix(), tableInfo.getName());

            File file = new File(filePath);
            if (!dtoConfig.isOverride() && file.exists()) return;

            Writer out = new FileWriter(file);
            Map<String, Object> paramsMap =new HashMap<>();
            paramsMap.put("global", globalConfig);
            paramsMap.put("dto", dtoConfig);
            paramsMap.put("table", tableInfo);

            dtoTemplate.process(paramsMap, out);
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException("文件生成失败["+ filePath +"]" + ex.getMessage());
        }
    }

    public void writeMapper(TableInfo tableInfo) {
        String filePath = "";
        try {
            filePath = getFilePath(mapperConfig.getPackageName(), mapperConfig.getSuffix(), tableInfo.getName());

            File file = new File(filePath);
            if (!mapperConfig.isOverride() && file.exists()) return;

            Writer out = new FileWriter(file);
            Map<String, Object> paramsMap =new HashMap<>();
            paramsMap.put("global", globalConfig);
            paramsMap.put("entity", entityConfig);
            paramsMap.put("mapper", mapperConfig);
            paramsMap.put("table", tableInfo);

            mapperTemplate.process(paramsMap, out);
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException("文件生成失败["+ filePath +"]" + ex.getMessage());
        }
    }


    public void writeService(TableInfo tableInfo) {
        String filePath = "";
        try {
            filePath = getFilePath(serviceConfig.getPackageName(), serviceConfig.getSuffix(), tableInfo.getName());

            File file = new File(filePath);
            if (!serviceConfig.isOverride() && file.exists()) return;

            Writer out = new FileWriter(file);
            Map<String, Object> paramsMap =new HashMap<>();
            paramsMap.put("global", globalConfig);
            paramsMap.put("dto", dtoConfig);
            paramsMap.put("entity", entityConfig);
            paramsMap.put("service", serviceConfig);
            paramsMap.put("table", tableInfo);

            serviceTemplate.process(paramsMap, out);
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException("文件生成失败["+ filePath +"]" + ex.getMessage());
        }
    }

    public void writeServiceImpl(TableInfo tableInfo) {
        String filePath = "";
        try {
            filePath = getFilePath(serviceImplConfig.getPackageName(), serviceImplConfig.getSuffix(), tableInfo.getName());

            File file = new File(filePath);
            if (!serviceImplConfig.isOverride() && file.exists()) return;

            Writer out = new FileWriter(file);
            Map<String, Object> paramsMap =new HashMap<>();
            paramsMap.put("global", globalConfig);
            paramsMap.put("dto", dtoConfig);
            paramsMap.put("entity", entityConfig);
            paramsMap.put("service", serviceConfig);
            paramsMap.put("serviceImpl", serviceImplConfig);
            paramsMap.put("mapper", mapperConfig);
            paramsMap.put("table", tableInfo);

            serviceImplTemplate.process(paramsMap, out);
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException("文件生成失败["+ filePath +"]" + ex.getMessage());
        }
    }


    public void writeController(TableInfo tableInfo) {
        String filePath = "";
        try {
            filePath = getFilePath(controllerConfig.getPackageName(), controllerConfig.getSuffix(), tableInfo.getName());

            File file = new File(filePath);
            if (!controllerConfig.isOverride() && file.exists()) return;

            Writer out = new FileWriter(file);
            Map<String, Object> paramsMap =new HashMap<>();
            paramsMap.put("global", globalConfig);
            paramsMap.put("dto", dtoConfig);
            paramsMap.put("entity", entityConfig);
            paramsMap.put("service", serviceConfig);
            paramsMap.put("serviceImpl", serviceImplConfig);
            paramsMap.put("controller", controllerConfig);
            paramsMap.put("table", tableInfo);

            controllerTemplate.process(paramsMap, out);
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException("文件生成失败["+ filePath +"]" + ex.getMessage());
        }
    }
}
