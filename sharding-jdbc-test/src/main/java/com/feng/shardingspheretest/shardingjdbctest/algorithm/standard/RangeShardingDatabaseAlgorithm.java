package com.feng.shardingspheretest.shardingjdbctest.algorithm.standard;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class RangeShardingDatabaseAlgorithm implements RangeShardingAlgorithm<Integer> {

    @Override
    public Collection<String> doSharding(final Collection<String> databaseNames, final RangeShardingValue<Integer> shardingValueRange) {
        Set<String> result = new LinkedHashSet<>();
        if (Range.closed(0, 50000).encloses(shardingValueRange.getValueRange())) {
            for (String each : databaseNames) {
                if (each.endsWith("0")) {
                    result.add(each);
                }
            }
        } else if (Range.closed(50001, 100000).encloses(shardingValueRange.getValueRange())) {
            for (String each : databaseNames) {
                if (each.endsWith("1")) {
                    result.add(each);
                }
            }
        } else if (Range.closed(0, 100000).encloses(shardingValueRange.getValueRange())) {
            result.addAll(databaseNames);
        } else {
            throw new UnsupportedOperationException();
        }
        return result;
    }

}
