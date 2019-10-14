package com.feng.shardingspheretest.shardingjdbctest.service;

import com.feng.shardingspheretest.shardingjdbctest.domain.Order;
import com.feng.shardingspheretest.shardingjdbctest.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    public List<Order> getListBysql(String sql){
        return orderMapper.getListBysql(sql);
    }

    public List<Order> getByOrderId(int orderId){
        return orderMapper.getByOrderId(orderId);
    }

    public List<Order> getByUserId(int userId){
        return orderMapper.getByUserId(userId);
    }

    public List<Order> getByOrderIdAndUserId(int orderId, int userId){
        return orderMapper.getByUserIdAndOrderId(orderId, userId);
    }

    public List<Order> getAll(){
        return orderMapper.getAll();
    }

    public int add(int orderId, int userId){
        return orderMapper.add(orderId, userId);
    }

    public List<Order> getByRangeOrderId(int orderIdStart, int orderIdEnd) {
        return orderMapper.getByRangeOrderId(orderIdStart, orderIdEnd);
    }

    public List<Order> getByRangeUserId(int userIdStart, int userIdEnd) {
        return orderMapper.getByRangeUserId(userIdStart, userIdEnd);
    }

    public List<Order> getByRangeOrderIdAndUserId(int orderIdStart, int orderIdEnd, int userIdStart, int userIdEnd) {
        return orderMapper.getByRangeOrderIdAndUserId(orderIdStart, orderIdEnd, userIdStart, userIdEnd);
    }
}
