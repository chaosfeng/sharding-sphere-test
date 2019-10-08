package com.feng.shardingspheretest.domain;

import lombok.Data;

@Data
public class Order {

    private String orderId;

    private Integer userID;

    private String description;

    public Order() {

    }

    public Order(String orderId, int userID) {
        this.orderId = orderId;
        this.userID = userID;
    }

}
