package com.feng.shardingspheretest;

import com.feng.shardingspheretest.domain.Order;
import com.feng.shardingspheretest.service.JDBCService;
import com.feng.shardingspheretest.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)// 按代码中方法的先后顺序执行
public class ShardingSphereTest {

    @Autowired
    OrderService orderService;

    @Before
    public void clearTables() {
        JDBCService.clear("master0", "t_order_0");
        JDBCService.clear("master0", "t_order_1");
        JDBCService.clear("master1", "t_order_0");
        JDBCService.clear("master1", "t_order_1");

        JDBCService.clear("master0slave0", "t_order_0");
        JDBCService.clear("master0slave0", "t_order_1");

        JDBCService.clear("master0slave1", "t_order_0");
        JDBCService.clear("master0slave1", "t_order_1");

        JDBCService.clear("master1slave0", "t_order_0");
        JDBCService.clear("master1slave0", "t_order_1");

        JDBCService.clear("master0slave1", "t_order_0");
        JDBCService.clear("master0slave1", "t_order_1");
    }

    // 读写分离（数据库未配置主从同步） + 分库分表，插入测试
    @Test
    public void insertTest() {
        int result1 = orderService.add(0, 0);// 应插入master0.t_order_0
        Assert.assertEquals(1, result1);
        int result2 = orderService.add(1, 0);// 应插入master0.t_order_1
        Assert.assertEquals(1, result2);
        int result3 = orderService.add(0, 1);// 应插入master1.t_order_0
        Assert.assertEquals(1, result3);
        int result4 = orderService.add(1, 1);// 应插入master1.t_order_1
        Assert.assertEquals(1, result4);

        List<Order> orderList1 = JDBCService.getAll("master0", "t_order_0");
        Assert.assertEquals(1, orderList1.size());
        Assert.assertEquals(0, orderList1.get(0).getOrderId());
        Assert.assertEquals(0, orderList1.get(0).getUserID());
        Assert.assertEquals("master0.t_order_0", orderList1.get(0).getDescription());

        List<Order> orderList2 = JDBCService.getAll("master0", "t_order_1");
        Assert.assertEquals(1, orderList2.size());
        Assert.assertEquals(1, orderList2.get(0).getOrderId());
        Assert.assertEquals(0, orderList2.get(0).getUserID());
        Assert.assertEquals("master0.t_order_1", orderList2.get(0).getDescription());

        List<Order> orderList3 = JDBCService.getAll("master1", "t_order_0");
        Assert.assertEquals(1, orderList3.size());
        Assert.assertEquals(0, orderList3.get(0).getOrderId());
        Assert.assertEquals(1, orderList3.get(0).getUserID());
        Assert.assertEquals("master1.t_order_0", orderList3.get(0).getDescription());

        List<Order> orderList4 = JDBCService.getAll("master1", "t_order_1");
        Assert.assertEquals(1, orderList4.size());
        Assert.assertEquals(1, orderList4.get(0).getOrderId());
        Assert.assertEquals(1, orderList4.get(0).getUserID());
        Assert.assertEquals("master1.t_order_1", orderList4.get(0).getDescription());
    }

    // 读写分离（主从未配置同步） + 分库分表，查询测试---依赖插入测试的数据
    @Test
    public void queryTableByTwoColumnTest() {
        // todo
        List<Order> orderList1 = orderService.getByUserIdAndOrderId(0, 0);

        List<Order> orderList2 = orderService.getByUserIdAndOrderId(1, 0);

        List<Order> orderList3 = orderService.getByUserIdAndOrderId(0, 1);

        List<Order> orderList4 = orderService.getByUserIdAndOrderId(1, 1);
    }

    // 读写分离（主从未配置同步） + 分库分表，查询测试---依赖插入测试的数据
    @Test
    public void queryTableByOneColumnTest() {
        // todo
        List<Order> orderList1 = orderService.getByOrderId(0);

        List<Order> orderList2 = orderService.getByOrderId(1);

        List<Order> orderList3 = orderService.getByUserId(0);

        List<Order> orderList4 = orderService.getByUserId(1);
    }

}
