package com.feng.shardingspheretest.sharding.standard;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class PreciseShardingTableAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Integer> shardingValue) {
        if (Range.closed(0, 50000).contains(shardingValue.getValue())) {
            for (String each : tableNames) {
                if (each.endsWith("0")) {
                    return each;
                }
            }
        } else if (Range.closed(50001, 100000).contains(shardingValue.getValue())) {
            for (String each : tableNames) {
                if (each.endsWith("1")) {
                    return each;
                }
            }
        }
        throw new UnsupportedOperationException();
    }

}
