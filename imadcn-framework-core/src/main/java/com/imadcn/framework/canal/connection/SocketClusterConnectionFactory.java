package com.imadcn.framework.canal.connection;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

public class SocketClusterConnectionFactory extends AbstractConnectionFactory {
	
	private String socketServers; //pattern : host:port,host:port,host:port

	@Override
	public CanalConnector createCanalConnector() {
		CanalConnector connector = CanalConnectors.newClusterConnector(getSocketAddress(socketServers), getDestination(), getUsername(), getPassword());
		return connector;
	}

	public void setSocketServers(String socketServers) {
		this.socketServers = socketServers;
	}
	
	protected List<SocketAddress> getSocketAddress(String socketServers) {
		Assert.notNull(socketServers, "Socket Servers must not be null");
		List<SocketAddress> socketAddresses = new ArrayList<>();
		String[] servers = socketServers.split(",");
		for (String srv : servers) {
			String hostname = srv.split(":")[0];
			int port = Integer.parseInt(srv.split(":")[1]);
			socketAddresses.add(new InetSocketAddress(hostname, port));
		}
		return socketAddresses;
	}
}
