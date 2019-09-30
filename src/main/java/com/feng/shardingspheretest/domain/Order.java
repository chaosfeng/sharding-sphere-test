package com.feng.shardingspheretest.domain;

import lombok.Data;

@Data
public class Order {

    private int orderId;

    private int userID;

    private String description;

}
