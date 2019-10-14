package com.feng.shardingspheretest.shardingjdbctest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.feng.shardingspheretest.shardingjdbctest.mapper","com.feng.shardingspheretest.shardingjdbctest.service"})
public class ShardingJDBCTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJDBCTestApplication.class, args);
    }

}
