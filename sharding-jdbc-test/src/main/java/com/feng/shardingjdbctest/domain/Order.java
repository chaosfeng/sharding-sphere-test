package com.feng.shardingjdbctest.domain;

public class Order {

    private String orderId;

    private Integer userId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Order() {

    }

    public Order(String orderId, int userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

}
