package com.feng.shardingspheretest.shardingjdbctest.transaction;

import com.feng.shardingspheretest.shardingjdbctest.domain.Order;
import com.feng.shardingspheretest.shardingjdbctest.exception.MyException;
import com.feng.shardingspheretest.shardingjdbctest.service.JDBCService;
import com.feng.shardingspheretest.shardingjdbctest.service.OrderService;
import com.feng.shardingspheretest.shardingjdbctest.service.SagaTransactionService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * saga事务测试，结合分库分表（没有主从）
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("inlineSharding")
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按方法名称排序执行
public class SagaWithShardingOnlyTest {

    @Autowired
    SagaTransactionService sagaTransactionService;

    @Autowired
    OrderService orderService;

    @Before
    @After
    public void clear() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }

    @Test
    public void testSagaTransactionSuccess() {
        sagaTransactionService.processSuccess();
        List<Order> orderList = orderService.getAll();
        Assert.assertEquals(4, orderList.size());
    }

    @Test
    public void testSagaTransactionFailure() {
        try {
            sagaTransactionService.processFailure();
        } catch (MyException E) {
            // do nothing
        }
        List<Order> orderList = orderService.getAll();
        Assert.assertTrue(orderList == null || orderList.isEmpty());
    }


}
