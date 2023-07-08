package com.easybbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = {"com.easybbs.mappers"})
@EnableTransactionManagement
public class EasybbsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasybbsWebApplication.class, args);
    }
}
