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

    public List<Order> getById(int id){
        return orderMapper.getById(id);
    }

}
