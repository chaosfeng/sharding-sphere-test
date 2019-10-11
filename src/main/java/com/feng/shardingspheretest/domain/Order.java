package com.feng.shardingspheretest.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_order")
public class Order {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "description")
    private String description;

    public Order() {

    }

    public Order(String orderId, int userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

}
