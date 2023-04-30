package com.mybatiseasy.generator;

import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.generator.config.*;
import com.mybatiseasy.generator.pojo.ColumnAutoSet;
import com.mybatiseasy.generator.pojo.TableInfo;
import com.mybatiseasy.keygen.NoKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class MybatisEasyGeneratorApplication {



    public static void main(String[] args) {

        String url = "jdbc:mysql://dev.tanxin.info:3306/tanxin-db?allowMultiQueries=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true";
        String username = "tanxin";
        String password = "asEedio234#r3";

        String baseDir = "D:/temp";

        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(url, username, password).build();
        GlobalConfig globalConfig = new GlobalConfig.Builder(baseDir, "com.mybatiseasy")
                .templateType(TemplateType.ALL)
                .idType(TableIdType.CUSTOM)
                .keyGenerator(NoKeyGenerator.class)
                .build();

        EntityConfig entityConfig = new EntityConfig.Builder("entity", "Entity")
                .override(true)
                .swagger(true)
                .enableLombok(false)
                .chain(true)
                .columnAutoSet(new ColumnAutoSet().setName("createTime").setInsert("NOW()"))
                .columnAutoSet(new ColumnAutoSet().setName("updateTime").setInsert("NOW()").setUpdate("NOW()"))
                .build();

        MapperConfig mapperConfig = new MapperConfig.Builder("mapper", "Mapper")
                .override(true)
                .build();

        DtoConfig dtoConfig = new DtoConfig.Builder("dto", "Dto")
                .override(true)
                .swagger(true)
                .build();

        new Generator()
                .dataSourceConfig(dataSourceConfig)
                .globalConfig(globalConfig)
                .entityConfig(entityConfig)
                .mapperConfig(mapperConfig)
//                .controllerConfig(controllerConfig)
                .dtoConfig(dtoConfig)
//                .serviceConfig(serviceConfig)
//                .serviceImplConfig(serviceImplConfig)
                .generate();

        SpringApplication.run(MybatisEasyGeneratorApplication.class, args);
    }

}
