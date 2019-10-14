package com.feng.shardingspheretest.shardingjdbctest.domain;

import lombok.Data;

@Data
public class User {

    private Integer userId;

    private Integer userCode;

    private String userName;

    private String description;

    public User() {

    }

    public User(int userId, int userCode, String userName) {
        this.userCode = userCode;
        this.userId = userId;
        this.userName = userName;
    }

}
