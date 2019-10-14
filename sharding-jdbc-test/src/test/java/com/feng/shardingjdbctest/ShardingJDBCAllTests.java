package com.feng.shardingjdbctest;

import com.feng.shardingjdbctest.sharding.ComplexShardingTest;
import com.feng.shardingjdbctest.sharding.HintShardingTest;
import com.feng.shardingjdbctest.sharding.RWSplittingCombineShardingTest;
import com.feng.shardingjdbctest.sharding.StandardShardingTest;
import com.feng.shardingjdbctest.sql.SQLTest;
import com.feng.shardingjdbctest.transaction.SagaWithRWSplittingCombineShardingTest;
import com.feng.shardingjdbctest.transaction.SagaWithShardingOnlyTest;
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
