package com.imadcn.framework.rdb.sharding.ddsj.algorithm.database;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;

public class SingleKeyDatabaseAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {

	public String doEqualSharding(Collection<String> dataSourceNames, ShardingValue<String> shardingValue) {
		for (String dataSource : dataSourceNames) {
			if (dataSource.endsWith(getShardingDataSource(shardingValue.getValue()))) {
				return dataSource;
			}
		}
		return null;
	}

	public Collection<String> doInSharding(Collection<String> dataSourceNames, ShardingValue<String> shardingValue) {
		Collection<String> result = new LinkedHashSet<String>(dataSourceNames.size());
		for (String value : shardingValue.getValues()) {
			for (String dataSource : dataSourceNames) {
				if (dataSource.endsWith(getShardingDataSource(value))) {
					result.add(dataSource);
				}
			}
		}
		return result;
	}

	public Collection<String> doBetweenSharding(Collection<String> dataSourceNames, ShardingValue<String> shardingValue) {
		return null;
	}

	private String getShardingDataSource(String value) {
		long num = Long.valueOf(value);
		return String.valueOf(num % 2);
	}

}
