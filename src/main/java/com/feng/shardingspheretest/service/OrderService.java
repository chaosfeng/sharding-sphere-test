package com.feng.shardingspheretest.service;

import com.feng.shardingspheretest.domain.Order;
import com.feng.shardingspheretest.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    public List<Order> getByOrderId(int orderId){
        return orderMapper.getByOrderId(orderId);
    }

    public List<Order> getByUserId(int userId){
        return orderMapper.getByUserId(userId);
    }

    public List<Order> getByUserIdAndOrderId(int orderId, int userId){
        return orderMapper.getByUserIdAndOrderId(orderId, userId);
    }

    public int add(int orderId, int userId){
        return orderMapper.add(orderId, userId);
    }
}
