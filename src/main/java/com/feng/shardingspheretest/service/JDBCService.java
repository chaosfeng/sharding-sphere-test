package com.feng.shardingspheretest.service;

import com.feng.shardingspheretest.domain.Order;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "JDBCService")
public class JDBCService {

    private final static String USER_NAME = "root";
    private final static String PASSWORD = "a";
    private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private final static String URL_PREFIX = "jdbc:mysql://localhost:3306/";

    public static Connection getConn(String dbName) throws Exception{
        //1 注册驱动
        Class.forName(DRIVER_NAME);
        //2 获得连接
        String url = URL_PREFIX + dbName;
        return DriverManager.getConnection(url, USER_NAME, PASSWORD);
    }

    public static boolean clear(String dbName, String tableName) {
        Connection conn = null;
        try {
            conn = getConn(dbName);
            //3获得语句执行者
            Statement st = conn.createStatement();
            //4执行SQL语句
            Boolean result = st.execute("delete from " + tableName);
            log.info("JDBC:delete from " + dbName + "." + tableName);
            //6释放资源
            st.close();
            conn.close();
            return result;
        } catch (Exception e) {
            log.error("原生JDBC执行计数sql错误:{}", e);
            return false;
        } finally {
            if(null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("原生JDBC关闭连接错误:{}", e);
                }
            }
        }
    }

    public static List<Order> getAll(String dbName, String tableName) {
        Connection conn = null;
        try {
            conn = getConn(dbName);
            //3获得语句执行者
            Statement st = conn.createStatement();
            //4执行SQL语句
            ResultSet rs = st.executeQuery("select * from " + tableName);
            log.info("JDBC:select count(1) from " + dbName + "." + tableName);
            //5处理结果集
            List<Order> orderList = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserID(rs.getInt("user_id"));
                order.setDescription(rs.getString("description"));
                orderList.add(order);
            }
            //6释放资源
            rs.close();
            st.close();
            conn.close();
            return orderList;
        } catch (Exception e) {
            log.error("原生JDBC执行查询sql错误:{}", e);
            return null;
        } finally {
            if(null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("原生JDBC关闭连接错误:{}", e);
                }
            }
        }
    }

    public static Boolean add(String dbName, String tableName, Order order) {
        Connection conn = null;
        try {
            conn = getConn(dbName);
            //3获得语句执行者
            Statement st = conn.createStatement();
            //4执行SQL语句
            Boolean result = st.execute("" + tableName);
            log.info("JDBC:inser into " + dbName + "." + tableName + "--->" + order.toString());
            //6释放资源
            st.close();
            conn.close();
            return result;
        } catch (Exception e) {
            log.error("原生JDBC执行新增sql错误:{}", e);
            return false;
        } finally {
            if(null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("原生JDBC关闭连接错误:{}", e);
                }
            }
        }
    }

}
