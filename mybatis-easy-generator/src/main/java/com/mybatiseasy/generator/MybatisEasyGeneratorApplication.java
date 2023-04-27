package com.mybatiseasy.generator;

import com.mybatiseasy.generator.config.DataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MybatisEasyGeneratorApplication {

    public static void main(String[] args) {
        DataSourceConfig config = new DataSourceConfig();

        Entry entry = new Entry();
        entry.datasource()
                .globalConfig()
                .entityConfig()
                        .mapperConfig()
                                .serviceConfig()
                                        .serviceImplConfig()
                                                .generate();

        SpringApplication.run(MybatisEasyGeneratorApplication.class, args);
    }

}
