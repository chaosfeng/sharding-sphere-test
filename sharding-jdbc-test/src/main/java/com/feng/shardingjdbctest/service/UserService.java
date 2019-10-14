package com.feng.shardingjdbctest.service;

import com.feng.shardingjdbctest.mapper.UserMapper;
import com.feng.shardingjdbctest.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public List<User> getListBysql(String sql){
        return userMapper.getListBysql(sql);
    }

    public List<User> getByUserId(int userId){
        return userMapper.getByUserId(userId);
    }

    public int add(int userId, int userCode, String userName){
        return userMapper.add(userId, userCode, userName);
    }

    public List<User> getAll(){
        return userMapper.getAll();
    }

}
