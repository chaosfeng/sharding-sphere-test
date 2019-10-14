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
 * Saga事务测试，结合主从+分库分表
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("RWSplittingCombineSharding")
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按方法名称排序执行
public class SagaWithRWSplittingCombineShardingTest {

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

    // bug here
    // 4.0.0-RC2版本
    // algorithm-sphere使用了saga思想，在底层sharding-sphere自动生成反向补偿SQL，因此要求插入操作必须包含主键信息（配置由sharding-jdbc生成主键也可以）。
    // 结合主从模式时，如果配置的master-slave-rules中的逻辑数据源名称与实际的master名称不一致，则每次插入都会失败，错误提示中有Cause: java.lang.IllegalStateException: Could not found primary key values
    // 原因是底层进行反向SQL生成时，有以下逻辑
    // io.shardingsphere.transaction.base.hook.revert.executor.insert.InsertSQLRevertContext.class
    // private boolean isRoutedDataNode(final List<DataNode> dataNodes, final DataNode dataNode) {
    //     for (DataNode each : dataNodes) {
    //         if (each.equals(dataNode)) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    // 但dataNodes中的数据库名称用的逻辑数据源名称，而dataNode中的名称用的实际的master的名称。此处的结果导致生成反向SQL时永远找不到主键，故Saga事务管理器直接回滚抛错。
    // dataNodes的来源暂未去详细定位。
    @Test
    public void testSagaTransactionSuccess() {
        sagaTransactionService.processSuccess();
        List<Order> orderList = orderService.getAll();
        Assert.assertEquals(4, orderList.size());
    }

    // bug here
    // 同上
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
