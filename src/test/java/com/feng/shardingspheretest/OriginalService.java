package com.feng.shardingspheretest;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j(topic = "OriginalService")
public class OriginalService {

    public static int getCount(String dbName, String tableName) throws Exception {
        //1 注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2 获得连接
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        Connection conn = DriverManager.getConnection(url, "root", "a");
        //3获得语句执行者
        Statement st = conn.createStatement();
        //4执行SQL语句
        ResultSet rs = st.executeQuery("select count(1) from " + tableName);
        log.info("select count(1) from " + dbName + "." + tableName);
        //5处理结果集
        int count = -1;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        //6释放资源
        rs.close();
        st.close();
        conn.close();
        return count;
    }

}
