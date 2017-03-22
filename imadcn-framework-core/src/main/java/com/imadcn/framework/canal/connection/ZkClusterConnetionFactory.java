package com.imadcn.framework.canal.connection;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

public class ZkClusterConnetionFactory extends AbstractConnectionFactory {
	
	private String zkServers; //pattern : host:port,host:port,host:port

	@Override
	public CanalConnector createCanalConnector() {
		CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, getDestination(), getUsername(), getPassword());
		return connector;
	}

	public void setZkServers(String zkServers) {
		this.zkServers = zkServers;
	}
}
