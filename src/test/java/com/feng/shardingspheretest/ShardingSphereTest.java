package com.feng.shardingspheretest;

import com.feng.shardingspheretest.domain.Order;
import com.feng.shardingspheretest.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingSphereTest {

    @Autowired
    OrderService orderService;

    @Before
    public void initTables() {

    }

    // 读写分离（主从未配置同步） + 分库分表，插入测试
    @Test
    public void insertTest() {
        int id = 0;
        List<Order> order = orderService.getById(id);
        Assert.assertEquals(1, order.size());
        Assert.assertEquals("1,1,master0slave0.t_order_0", order.get(0).getDescription());
    }

}
