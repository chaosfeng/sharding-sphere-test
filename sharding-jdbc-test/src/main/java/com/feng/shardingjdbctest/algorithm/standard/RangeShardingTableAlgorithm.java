package com.feng.shardingjdbctest.algorithm.standard;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class RangeShardingTableAlgorithm implements RangeShardingAlgorithm<Integer> {

    @Override
    public Collection<String> doSharding(final Collection<String> tableNames, final RangeShardingValue<Integer> shardingValueRange) {
        Set<String> result = new LinkedHashSet<>();
        if (Range.closed(0, 50000).encloses(shardingValueRange.getValueRange())) {
            for (String each : tableNames) {
                if (each.endsWith("0")) {
                    result.add(each);
                }
            }
        } else if (Range.closed(50001, 100000).encloses(shardingValueRange.getValueRange())) {
            for (String each : tableNames) {
                if (each.endsWith("1")) {
                    result.add(each);
                }
            }
        } else if (Range.closed(0, 100000).encloses(shardingValueRange.getValueRange())) {
            result.addAll(tableNames);
        } else {
            throw new UnsupportedOperationException();
        }
        return result;
    }

}
