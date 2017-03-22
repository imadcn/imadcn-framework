package com.imadcn.framework.canal.connection;

import java.net.InetSocketAddress;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

public class SimpleConnectionFactory extends AbstractConnectionFactory {
	
	private String host;
	private int port;

	@Override
	public CanalConnector createCanalConnector() {
		CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(host, port), getDestination(), getUsername(), getPassword());
		return connector;
	}
}
