package com.feng.shardingspheretest.shardingjdbctest.exception;

/**
 * 自定义异常
 * @desc 用于模拟异常，缩小异常范围
 */
public class MyException extends RuntimeException {

    public MyException(String msg) {
        super(msg);
    }
}
