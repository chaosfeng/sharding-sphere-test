package com.feng.shardingspheretest.domain;

import lombok.Data;

@Data
public class Order {

    private String orderId;

    private Integer userId;

    private String description;

    public Order() {

    }

    public Order(String orderId, int userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

}
