package com.imadcn.test.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class FirstJob implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {
		int item = shardingContext.getShardingItem();
		System.out.println(item);
	}

}
