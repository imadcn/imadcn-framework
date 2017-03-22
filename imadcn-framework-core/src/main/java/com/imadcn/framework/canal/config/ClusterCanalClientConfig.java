package com.imadcn.framework.canal.config;

public class ClusterCanalClientConfig extends CanalClientConfig {

	private String zkServers;
	
	public ClusterCanalClientConfig() {}

	public ClusterCanalClientConfig(String destination, String username, String password, int fetchSize, String zkServers) {
		this.destination = destination;
		this.username = username;
		this.password = password;
		this.fetchSize = fetchSize;
		this.zkServers = zkServers;
	}
	public String getZkServers() {
		return zkServers;
	}

	public void setZkServers(String zkServers) {
		this.zkServers = zkServers;
	}
}
