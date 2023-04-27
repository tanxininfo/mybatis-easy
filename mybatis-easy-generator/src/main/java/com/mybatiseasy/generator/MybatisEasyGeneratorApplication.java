package com.mybatiseasy.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MybatisEasyGeneratorApplication {

    public static void main(String[] args) {

        Entry entry = new Entry();
        entry.datasource();

        SpringApplication.run(MybatisEasyGeneratorApplication.class, args);
    }

}
