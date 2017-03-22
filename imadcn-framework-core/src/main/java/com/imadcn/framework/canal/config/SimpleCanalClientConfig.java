package com.imadcn.framework.canal.config;

public class SimpleCanalClientConfig extends CanalClientConfig {
	private String host;
	private int port;

	public SimpleCanalClientConfig() {}
	
	public SimpleCanalClientConfig(String destination, String username, String password, int fetchSize, String host, int port) {
		this.destination = destination;
		this.username = username;
		this.password = password;
		this.fetchSize = fetchSize;
		this.host = host;
		this.port = port;
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

}
