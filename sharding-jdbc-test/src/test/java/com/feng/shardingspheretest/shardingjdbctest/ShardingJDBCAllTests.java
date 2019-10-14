package com.feng.shardingspheretest.shardingjdbctest;

import com.feng.shardingspheretest.shardingjdbctest.sharding.ComplexShardingTest;
import com.feng.shardingspheretest.shardingjdbctest.sharding.HintShardingTest;
import com.feng.shardingspheretest.shardingjdbctest.sharding.RWSplittingCombineShardingTest;
import com.feng.shardingspheretest.shardingjdbctest.sharding.StandardShardingTest;
import com.feng.shardingspheretest.shardingjdbctest.sql.SQLTest;
import com.feng.shardingspheretest.shardingjdbctest.transaction.SagaWithRWSplittingCombineShardingTest;
import com.feng.shardingspheretest.shardingjdbctest.transaction.SagaWithShardingOnlyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ComplexShardingTest.class,
        HintShardingTest.class,
        RWSplittingCombineShardingTest.class,
        StandardShardingTest.class,
        SQLTest.class,
        SagaWithRWSplittingCombineShardingTest.class,
        SagaWithShardingOnlyTest.class
})
public class ShardingJDBCAllTests {

}
