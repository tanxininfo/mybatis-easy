package com.mybatiseasy.generator.template;

import com.mybatiseasy.generator.config.*;
import com.mybatiseasy.generator.pojo.TableInfo;

public interface ITemplate {
    void init(GlobalConfig globalConfig,
              EntityConfig entityConfig,
              DtoConfig dtoConfig,
              MapperConfig mapperConfig,
              ControllerConfig controllerConfig,
              ServiceConfig serviceConfig,
              ServiceImplConfig serviceImplConfig
              );

    void writeEntity(TableInfo tableInfo);
    void writeMapper(TableInfo tableInfo);
    void writeDto(TableInfo tableInfo);
}