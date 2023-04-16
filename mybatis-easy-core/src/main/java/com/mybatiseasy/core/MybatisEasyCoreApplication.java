package com.mybatiseasy.core;

import com.mybatiseasy.core.session.MyConfiguration;
import com.mybatiseasy.core.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
@EnableScheduling
@MapperScan("com.mybatiseasy.core.mapper")
public class MybatisEasyCoreApplication {


    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    //创建SqlSessionFactoryBean对象
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        MyConfiguration configuration = new MyConfiguration();
        configuration.setLogImpl(StdOutImpl.class);//标准输出日志
        configuration.setMapUnderscoreToCamelCase(true);// 开启驼峰命名
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSource dataSource(){
        //spring-jdbc中的数据源
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driver);
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        return driverManagerDataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(MybatisEasyCoreApplication.class, args);
    }

}
