package com.lizhen;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.activiti.spring.boot.DataSourceProcessEngineAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableDubboConfig
@ComponentScan("com.lizhen")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, DataSourceProcessEngineAutoConfiguration.class})
@SpringBootApplication
public class ActivitiWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiWebApplication.class, args);
    }

}

