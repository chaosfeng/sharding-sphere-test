package com.feng.shardingspheretest.test.transaction;

import com.feng.shardingspheretest.domain.Order;
import com.feng.shardingspheretest.service.JDBCService;
import com.feng.shardingspheretest.service.SagaTransactionalService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * saga
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("RWSplittingCombineSharding")
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按方法名称排序执行
public class SagaTest {

    @Autowired
    SagaTransactionalService saga;

    @BeforeClass
    public static void init() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }

    @Test
    public void test() {
        saga.processSuccess();
        try {
            saga.processFailure();
        } catch (Exception E) {

        }
        List<Order> all = JDBCService.getAllOrders("master0", "t_order_0");
        System.out.println("all-----------------------------" + all);
    }

    @AfterClass
    public static void clear() {
        // 清理旧数据
        JDBCService.clearOrderInAllDB();
    }

}
