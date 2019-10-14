package com.feng.shardingjdbctest.sharding;

import com.feng.shardingjdbctest.domain.Order;
import com.feng.shardingjdbctest.service.JDBCService;
import com.feng.shardingjdbctest.service.OrderService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 分库分表(complexSharding) 读写测试
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("complexSharding")
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按方法名称排序执行
public class ComplexShardingTest {

    @Autowired
    OrderService orderService;

    @BeforeClass
    public static void init() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }

    // 此方法需第一个执行，后续测试依赖此处插入的数据
    @Test
    public void insertOrderTest() {
        int result1 = orderService.add(0, 0);// 应插入master0.t_order_0
        Assert.assertEquals(1, result1);
        int result2 = orderService.add(1, 0);// 应插入master0.t_order_1
        Assert.assertEquals(1, result2);
        int result3 = orderService.add(0, 1);// 应插入master1.t_order_0
        Assert.assertEquals(1, result3);
        int result4 = orderService.add(1, 1);// 应插入master1.t_order_1
        Assert.assertEquals(1, result4);

        // 使用原生JDBC查询单表，验证数据插入逻辑是否正确
        List<Order> orderList1 = JDBCService.getAllOrders("master0", "t_order_0");
        Assert.assertEquals(1, orderList1.size());
        Assert.assertEquals("0", orderList1.get(0).getOrderId());
        Assert.assertEquals(0, orderList1.get(0).getUserId().intValue());
        Assert.assertEquals("master0.t_order_0", orderList1.get(0).getDescription());

        List<Order> orderList2 = JDBCService.getAllOrders("master0", "t_order_1");
        Assert.assertEquals(1, orderList2.size());
        Assert.assertEquals("1", orderList2.get(0).getOrderId());
        Assert.assertEquals(0, orderList2.get(0).getUserId().intValue());
        Assert.assertEquals("master0.t_order_1", orderList2.get(0).getDescription());

        List<Order> orderList3 = JDBCService.getAllOrders("master1", "t_order_0");
        Assert.assertEquals(1, orderList3.size());
        Assert.assertEquals("0", orderList3.get(0).getOrderId());
        Assert.assertEquals(1, orderList3.get(0).getUserId().intValue());
        Assert.assertEquals("master1.t_order_0", orderList3.get(0).getDescription());

        List<Order> orderList4 = JDBCService.getAllOrders("master1", "t_order_1");
        Assert.assertEquals(1, orderList4.size());
        Assert.assertEquals("1", orderList4.get(0).getOrderId());
        Assert.assertEquals(1, orderList4.get(0).getUserId().intValue());
        Assert.assertEquals("master1.t_order_1", orderList4.get(0).getDescription());
    }

    // 分库分表，查询测试（以库分片键和表分片键为查询条件）
    @Test
    public void queryOrderByUserIdAndOrderIdTest() {
        List<Order> orderList11 = orderService.getByOrderIdAndUserId(0, 0);// 应查询master0的t_order_0
        Assert.assertEquals(1, orderList11.size());
        Assert.assertEquals("0", orderList11.get(0).getOrderId());
        Assert.assertEquals(0, orderList11.get(0).getUserId().intValue());
        Assert.assertEquals("master0.t_order_0", orderList11.get(0).getDescription());
        
        List<Order> orderList21 = orderService.getByOrderIdAndUserId(1, 0);// 应查询master0的t_order_1
        Assert.assertEquals(1, orderList21.size());
        Assert.assertEquals("1", orderList21.get(0).getOrderId());
        Assert.assertEquals(0, orderList21.get(0).getUserId().intValue());
        Assert.assertEquals("master0.t_order_1", orderList21.get(0).getDescription());

        List<Order> orderList31 = orderService.getByOrderIdAndUserId(0, 1);// 应查询master1的t_order_0
        Assert.assertEquals(1, orderList31.size());
        Assert.assertEquals("0", orderList31.get(0).getOrderId());
        Assert.assertEquals(1, orderList31.get(0).getUserId().intValue());
        Assert.assertEquals("master1.t_order_0", orderList31.get(0).getDescription());

        List<Order> orderList41 = orderService.getByOrderIdAndUserId(1, 1);// 应查询master1的t_order_1
        Assert.assertEquals(1, orderList41.size());
        Assert.assertEquals("1", orderList41.get(0).getOrderId());
        Assert.assertEquals(1, orderList41.get(0).getUserId().intValue());
        Assert.assertEquals("master1.t_order_1", orderList41.get(0).getDescription());
        
    }

    // 分库分表，查询测试（以库分片键或表分片键为查询条件）
    @Test
    public void queryOrderByUserIdOrOrderIdTest() {
        List<Order> orderList1 = orderService.getByOrderId(0);// 应从master0的t_order_0和master1的t_order_0查询并合并数据
        Assert.assertEquals(2, orderList1.size());
        Assert.assertEquals("0", orderList1.get(0).getOrderId());
        Assert.assertEquals(0, orderList1.get(0).getUserId().intValue());
        Assert.assertEquals("master0.t_order_0", orderList1.get(0).getDescription());
        Assert.assertEquals("0", orderList1.get(1).getOrderId());
        Assert.assertEquals(1, orderList1.get(1).getUserId().intValue());
        Assert.assertEquals("master1.t_order_0", orderList1.get(1).getDescription());

        List<Order> orderList2 = orderService.getByOrderId(1);// 应从master0的t_order_1和master1的t_order_1查询并合并数据
        Assert.assertEquals(2, orderList2.size());
        Assert.assertEquals("1", orderList2.get(0).getOrderId());
        Assert.assertEquals(0, orderList2.get(0).getUserId().intValue());
        Assert.assertEquals("master0.t_order_1", orderList2.get(0).getDescription());
        Assert.assertEquals("1", orderList2.get(1).getOrderId());
        Assert.assertEquals(1, orderList2.get(1).getUserId().intValue());
        Assert.assertEquals("master1.t_order_1", orderList2.get(1).getDescription());

        List<Order> orderList3 = orderService.getByUserId(0);// 应从master0的t_order_0和master0的t_order_1查询并合并数据
        Assert.assertEquals(2, orderList2.size());
        Assert.assertEquals("0", orderList3.get(0).getOrderId());
        Assert.assertEquals(0, orderList3.get(0).getUserId().intValue());
        Assert.assertEquals("master0.t_order_0", orderList3.get(0).getDescription());
        Assert.assertEquals("1", orderList3.get(1).getOrderId());
        Assert.assertEquals(0, orderList3.get(1).getUserId().intValue());
        Assert.assertEquals("master0.t_order_1", orderList3.get(1).getDescription());

        List<Order> orderList4 = orderService.getByUserId(1);// 应从master1的t_order_0和master1的t_order_1查询并合并数据
        Assert.assertEquals(2, orderList2.size());
        Assert.assertEquals("0", orderList4.get(0).getOrderId());
        Assert.assertEquals(1, orderList4.get(0).getUserId().intValue());
        Assert.assertEquals("master1.t_order_0", orderList4.get(0).getDescription());
        Assert.assertEquals("1", orderList4.get(1).getOrderId());
        Assert.assertEquals(1, orderList4.get(1).getUserId().intValue());
        Assert.assertEquals("master1.t_order_1", orderList4.get(1).getDescription());
    }

    // 分库分表，普通查询测试
    @Test
    public void queryAllOrder() {
        List<Order> orderList = orderService.getAll();
        Assert.assertEquals(4, orderList.size());
    }

    @AfterClass
    public static void clear() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }
}
