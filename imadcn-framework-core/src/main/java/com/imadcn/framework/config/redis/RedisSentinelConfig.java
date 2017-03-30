package com.imadcn.framework.config.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;

public class RedisSentinelConfig extends RedisSentinelConfiguration implements InitializingBean {

	private String masterName;

	private String sentinelAddrs;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.master(masterName);
		String[] sentinelAddrArray = sentinelAddrs.split(";");
		for (String sentinelAddr : sentinelAddrArray) {
			String host = sentinelAddr.split(":")[0];
			int port = Integer.parseInt(sentinelAddr.split(":")[1]);
			super.sentinel(host, port);
		}
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getSentinelAddrs() {
		return sentinelAddrs;
	}

	public void setSentinelAddrs(String sentinelAddrs) {
		this.sentinelAddrs = sentinelAddrs;
	}

}
