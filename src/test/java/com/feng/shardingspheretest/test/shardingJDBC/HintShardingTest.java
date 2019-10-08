package com.feng.shardingspheretest.test.shardingJDBC;

import com.feng.shardingspheretest.domain.Order;
import com.feng.shardingspheretest.service.JDBCService;
import com.feng.shardingspheretest.service.OrderService;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 分库分表(hintSharding) 测试
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("hintSharding")
@SpringBootTest
public class HintShardingTest {

    @Autowired
    OrderService orderService;

    @BeforeClass
    public static void init() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }

    @Test
    public void insertOrderTest() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.addDatabaseShardingValue("t_order", 100);
        hintManager.addTableShardingValue("t_order", 100);

        int result1 = orderService.add(0, 0);// 应插入master0.t_order_0
        Assert.assertEquals(1, result1);

        // 使用原生JDBC查询单表，验证数据插入逻辑是否正确
        List<Order> orderList1 = JDBCService.getAllOrders("master0", "t_order_0");
        Assert.assertEquals(1, orderList1.size());
        Assert.assertEquals("0", orderList1.get(0).getOrderId());
        Assert.assertEquals(0, orderList1.get(0).getUserID().intValue());
        Assert.assertEquals("master0.t_order_0", orderList1.get(0).getDescription());
    }

    @AfterClass
    public static void clear() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }
}
