package com.feng.shardingspheretest.shardingjdbctest.algorithm.complex;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ComplexKeysShardingDatabaseAlgorithm implements ComplexKeysShardingAlgorithm {

    // 分片逻辑：user_id为偶数，用0结尾的数据库，否则用1结尾的数据库。
    @Override
    public Collection<String> doSharding(final Collection databaseNames, final ComplexKeysShardingValue info) {
        Set<String> result = new LinkedHashSet<>();
        Map<String, Collection> shardingValuesMap = info.getColumnNameAndShardingValuesMap();

        // 处理in和=
        if(shardingValuesMap.containsKey("user_id")) {
            Collection values = shardingValuesMap.get("user_id");
            for(Object value : values) {
                for (Object each : databaseNames) {
                    String dbName = (String) each;
                    if (dbName.endsWith((int)value % 2 + "")) {
                        result.add(dbName);
                    }
                }
            }
            return result;
        }

        return databaseNames;
    }
}
