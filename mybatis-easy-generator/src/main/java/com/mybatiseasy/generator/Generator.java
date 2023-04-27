package com.mybatiseasy.generator;

import com.mybatiseasy.generator.config.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class Generator {
    private DataSourceConfig dataSourceConfig;
    private GlobalConfig globalConfig;
    private EntityConfig entityConfig;
    private MapperConfig mapperConfig;
    private ServiceConfig serviceConfig;
    private ServiceImplConfig serviceImplConfig;
    private DtoConfig dtoConfig;
    private ControllerConfig controllerConfig;
    public Generator dataSourceConfig(DataSourceConfig dataSourceConfig){
         this.dataSourceConfig = dataSourceConfig;
        return this;
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


    private Connection getConnection(){
        try {
            Connection connection =
                    DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from user");
            ResultSetMetaData metaData = rs.getMetaData();
            return connection;

        }catch (Exception ex){
            log.error("出错啦" + ex.getMessage());
        }
    }
}
