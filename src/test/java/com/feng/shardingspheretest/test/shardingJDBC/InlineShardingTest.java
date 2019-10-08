package com.feng.shardingspheretest.test.shardingJDBC;

import com.feng.shardingspheretest.domain.Order;
import com.feng.shardingspheretest.service.JDBCService;
import com.feng.shardingspheretest.service.OrderService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 读写分离(主从未配置同步) + 分库分表(InlineShardingStrategy) 读写测试
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("inlineSharding")
@SpringBootTest
@FixMethodOrder(MethodSorters.JVM)// 按代码中方法的先后顺序执行
public class InlineShardingTest {

    @Autowired
    OrderService orderService;

    @Before
    public void init() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();

        // 使用原生JDBC向slave库插入测试所需数据
        JDBCService.addOrder("master0slave0", "t_order_0", new Order("0",0));
        JDBCService.addOrder("master0slave0", "t_order_1", new Order("1",0));

        JDBCService.addOrder("master0slave1", "t_order_0", new Order("0",0));
        JDBCService.addOrder("master0slave1", "t_order_1", new Order("1",0));

        JDBCService.addOrder("master1slave0", "t_order_0", new Order("0",1));
        JDBCService.addOrder("master1slave0", "t_order_1", new Order("1",1));

        JDBCService.addOrder("master1slave1", "t_order_0", new Order("0",1));
        JDBCService.addOrder("master1slave1", "t_order_1", new Order("1",1));
    }

    @After
    public void clear() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }

    // 读写分离（数据库未配置主从同步） + 分库分表，插入测试
    @Test
    public void insertOrderTest() {
        // 使用sharding-jdbc插入数据：均应写入master库
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
        Assert.assertEquals(0, orderList1.get(0).getUserID().intValue());
        Assert.assertEquals("master0.t_order_0", orderList1.get(0).getDescription());

        List<Order> orderList2 = JDBCService.getAllOrders("master0", "t_order_1");
        Assert.assertEquals(1, orderList2.size());
        Assert.assertEquals("1", orderList2.get(0).getOrderId());
        Assert.assertEquals(0, orderList2.get(0).getUserID().intValue());
        Assert.assertEquals("master0.t_order_1", orderList2.get(0).getDescription());

        List<Order> orderList3 = JDBCService.getAllOrders("master1", "t_order_0");
        Assert.assertEquals(1, orderList3.size());
        Assert.assertEquals("0", orderList3.get(0).getOrderId());
        Assert.assertEquals(1, orderList3.get(0).getUserID().intValue());
        Assert.assertEquals("master1.t_order_0", orderList3.get(0).getDescription());

        List<Order> orderList4 = JDBCService.getAllOrders("master1", "t_order_1");
        Assert.assertEquals(1, orderList4.size());
        Assert.assertEquals("1", orderList4.get(0).getOrderId());
        Assert.assertEquals(1, orderList4.get(0).getUserID().intValue());
        Assert.assertEquals("master1.t_order_1", orderList4.get(0).getDescription());
    }

    // 读写分离（主从未配置同步） + 分库分表，查询测试（以分库和分表字段为查询条件）
    @Test
    public void queryOrderByUserIdAndOrderIdTest() {
        // 使用sharding-jdbc查询数据：均应从slave库查询
        List<Order> orderList11 = orderService.getByUserIdAndOrderId(0, 0);// 应查询master0slave0的t_order_0
        Assert.assertEquals(1, orderList11.size());
        Assert.assertEquals("0", orderList11.get(0).getOrderId());
        Assert.assertEquals(0, orderList11.get(0).getUserID().intValue());
        Assert.assertEquals("master0slave0.t_order_0", orderList11.get(0).getDescription());

        List<Order> orderList12 = orderService.getByUserIdAndOrderId(0, 0);// 应查询master0slave1的t_order_0
        Assert.assertEquals(1, orderList12.size());
        Assert.assertEquals("0", orderList12.get(0).getOrderId());
        Assert.assertEquals(0, orderList12.get(0).getUserID().intValue());
        Assert.assertEquals("master0slave1.t_order_0", orderList12.get(0).getDescription());

        List<Order> orderList21 = orderService.getByUserIdAndOrderId(1, 0);// 应查询master0slave0的t_order_1
        Assert.assertEquals(1, orderList21.size());
        Assert.assertEquals("1", orderList21.get(0).getOrderId());
        Assert.assertEquals(0, orderList21.get(0).getUserID().intValue());
        Assert.assertEquals("master0slave0.t_order_1", orderList21.get(0).getDescription());

        List<Order> orderList22 = orderService.getByUserIdAndOrderId(1, 0);// 应查询master0slave1的t_order_1
        Assert.assertEquals(1, orderList22.size());
        Assert.assertEquals("1", orderList22.get(0).getOrderId());
        Assert.assertEquals(0, orderList22.get(0).getUserID().intValue());
        Assert.assertEquals("master0slave1.t_order_1", orderList22.get(0).getDescription());

        List<Order> orderList31 = orderService.getByUserIdAndOrderId(0, 1);// 应查询master1slave0的t_order_0
        Assert.assertEquals(1, orderList31.size());
        Assert.assertEquals("0", orderList31.get(0).getOrderId());
        Assert.assertEquals(1, orderList31.get(0).getUserID().intValue());
        Assert.assertEquals("master1slave0.t_order_0", orderList31.get(0).getDescription());

        List<Order> orderList32 = orderService.getByUserIdAndOrderId(0, 1);// 应查询master1slave1的t_order_0
        Assert.assertEquals(1, orderList32.size());
        Assert.assertEquals("0", orderList32.get(0).getOrderId());
        Assert.assertEquals(1, orderList32.get(0).getUserID().intValue());
        Assert.assertEquals("master1slave1.t_order_0", orderList32.get(0).getDescription());

        List<Order> orderList41 = orderService.getByUserIdAndOrderId(1, 1);// 应查询master1slave0的t_order_1
        Assert.assertEquals(1, orderList41.size());
        Assert.assertEquals("1", orderList41.get(0).getOrderId());
        Assert.assertEquals(1, orderList41.get(0).getUserID().intValue());
        Assert.assertEquals("master1slave0.t_order_1", orderList41.get(0).getDescription());

        List<Order> orderList42 = orderService.getByUserIdAndOrderId(1, 1);// 应查询master1slave1的t_order_1
        Assert.assertEquals(1, orderList42.size());
        Assert.assertEquals("1", orderList42.get(0).getOrderId());
        Assert.assertEquals(1, orderList42.get(0).getUserID().intValue());
        Assert.assertEquals("master1slave1.t_order_1", orderList42.get(0).getDescription());
    }

    // 读写分离（主从未配置同步） + 分库分表，查询测试（以分库或分表字段为查询条件）
    @Test
    public void queryOrderByUserIdOrOrderIdTest() {
        List<Order> orderList1 = orderService.getByOrderId(0);// 应从master0slave0的t_order_0和master1slave0的t_order_0查询并合并数据
        Assert.assertEquals(2, orderList1.size());
        Assert.assertEquals("0", orderList1.get(0).getOrderId());
        Assert.assertEquals(0, orderList1.get(0).getUserID().intValue());
        Assert.assertEquals("master0slave0.t_order_0", orderList1.get(0).getDescription());
        Assert.assertEquals("0", orderList1.get(1).getOrderId());
        Assert.assertEquals(1, orderList1.get(1).getUserID().intValue());
        Assert.assertEquals("master1slave0.t_order_0", orderList1.get(1).getDescription());

        List<Order> orderList2 = orderService.getByOrderId(1);// 应从master0slave1的t_order_1和master1slave1的t_order_1查询并合并数据
        Assert.assertEquals(2, orderList2.size());
        Assert.assertEquals("1", orderList2.get(0).getOrderId());
        Assert.assertEquals(0, orderList2.get(0).getUserID().intValue());
        Assert.assertEquals("master0slave1.t_order_1", orderList2.get(0).getDescription());
        Assert.assertEquals("1", orderList2.get(1).getOrderId());
        Assert.assertEquals(1, orderList2.get(1).getUserID().intValue());
        Assert.assertEquals("master1slave1.t_order_1", orderList2.get(1).getDescription());

        List<Order> orderList3 = orderService.getByUserId(0);// 应从master0slave0的t_order_0和master0slave1的t_order_1查询并合并数据
        Assert.assertEquals(2, orderList2.size());
        Assert.assertEquals("0", orderList3.get(0).getOrderId());
        Assert.assertEquals(0, orderList3.get(0).getUserID().intValue());
        Assert.assertEquals("master0slave0.t_order_0", orderList3.get(0).getDescription());
        Assert.assertEquals("1", orderList3.get(1).getOrderId());
        Assert.assertEquals(0, orderList3.get(1).getUserID().intValue());
        Assert.assertEquals("master0slave1.t_order_1", orderList3.get(1).getDescription());

        List<Order> orderList4 = orderService.getByUserId(1);// 应从master1slave0的t_order_0和master1slave1的t_order_1查询并合并数据
        Assert.assertEquals(2, orderList2.size());
        Assert.assertEquals("0", orderList4.get(0).getOrderId());
        Assert.assertEquals(1, orderList4.get(0).getUserID().intValue());
        Assert.assertEquals("master1slave0.t_order_0", orderList4.get(0).getDescription());
        Assert.assertEquals("1", orderList4.get(1).getOrderId());
        Assert.assertEquals(1, orderList4.get(1).getUserID().intValue());
        Assert.assertEquals("master1slave1.t_order_1", orderList4.get(1).getDescription());
    }

    // 读写分离（主从未配置同步） + 分库分表，普通查询测试
    @Test
    public void queryAllOrder() {
        List<Order> orderList = orderService.getAll();
        Assert.assertEquals(4, orderList.size());
    }

}
