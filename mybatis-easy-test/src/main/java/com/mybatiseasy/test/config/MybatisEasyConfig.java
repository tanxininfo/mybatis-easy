package com.mybatiseasy.test.config;

import com.mybatiseasy.core.config.GlobalConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.mybatiseasy.test.mapper"})
public class MybatisEasyConfig {

    public MybatisEasyConfig(){
        GlobalConfig.setTenantFactory(null);
    }
}
