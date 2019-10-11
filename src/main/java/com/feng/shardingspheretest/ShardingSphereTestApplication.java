package com.feng.shardingspheretest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

@SpringBootApplication(exclude = JtaAutoConfiguration.class, scanBasePackages = {"com.feng.shardingspheretest.mapper","com.feng.shardingspheretest.service"})
public class ShardingSphereTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingSphereTestApplication.class, args);
    }

}
