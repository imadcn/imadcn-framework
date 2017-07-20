package com.imadcn.framework.rdb.sharding.ddsj.algorithm.table;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;

public class SingleKeyTableAlgorithm implements SingleKeyTableShardingAlgorithm<String> {

	public String doEqualSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
		for (String table : tableNames) {
			if (table.endsWith(getShardingTable(shardingValue.getValue()))) {
				return table;
			}
		}
		return null;
	}

	public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
		Collection<String> result = new LinkedHashSet<String>(tableNames.size());
		for (String value : shardingValue.getValues()) {
			for (String table : tableNames) {
				if (table.endsWith(getShardingTable(value))) {
					result.add(table);
				}
			}
		}
		return result;
	}

	public Collection<String> doBetweenSharding(Collection<String> tableNames, ShardingValue<String> shardingValue) {
		return null;
	}

	private String getShardingTable(String value) {
		long num = Long.valueOf(value);
		return String.valueOf(num % 10);
	}

}
