package com.feng.shardingjdbctest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

@SpringBootApplication(exclude = JtaAutoConfiguration.class, scanBasePackages = {"com.feng.shardingjdbctest.mapper","com.feng.shardingjdbctest.service"})
public class ShardingJDBCTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJDBCTestApplication.class, args);
    }

}
