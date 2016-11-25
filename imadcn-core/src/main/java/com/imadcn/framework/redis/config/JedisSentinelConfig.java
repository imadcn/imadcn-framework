package com.imadcn.framework.redis.config;

import java.util.HashSet;
import java.util.Set;

public class JedisSentinelConfig {

	private String masterName;
	private String host;
	private int port;
	private String password;
	private Set<String> sentinelNodes;

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSentinelAddrs(String sentinelAddrs) {
		String[] sentinelAddrArray = sentinelAddrs.split(";");
		sentinelNodes = new HashSet<String>();
		for (String sentinelAddr : sentinelAddrArray) {
			sentinelNodes.add(sentinelAddr);
		}
	}

	public Set<String> getSentinelNodes() {
		return sentinelNodes;
	}
}
