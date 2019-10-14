package com.feng.shardingjdbctest.algorithm.complex;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ComplexKeysShardingTableAlgorithm implements ComplexKeysShardingAlgorithm {

    // 分片逻辑：order_id为偶数，用0结尾的表，否则用1结尾的数据表。
    @Override
    public Collection<String> doSharding(final Collection tableNames, final ComplexKeysShardingValue info) {
        Set<String> result = new LinkedHashSet<>();
        Map<String, Collection> shardingValuesMap = info.getColumnNameAndShardingValuesMap();

        // 处理in和=
        if(shardingValuesMap.containsKey("order_id")) {
            Collection values = shardingValuesMap.get("order_id");
            for(Object value : values) {
                for (Object each : tableNames) {
                    String tableName = (String) each;
                    if (tableName.endsWith((int)value % 2 + "")) {
                        result.add(tableName);
                    }
                }
            }
            return result;
        }

        return tableNames;
    }
}
