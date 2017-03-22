package com.imadcn.framework.canal.connection;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import com.alibaba.otter.canal.client.CanalConnector;

public abstract class AbstractConnectionFactory implements ConnectionFactory, DisposableBean, BeanNameAware {

	private CanalConnector connector;
	
	private String destination;
	private String username;
	private String password;
	private long soTimeout;
	private String filter;

	private volatile String beanName;

	public CanalConnector getConnector() {
		return connector;
	}

	public void setConnector(CanalConnector connector) {
		Assert.notNull(connector, "Target Connector must not be null");
		this.connector = connector;
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public String getDestination() {
		return this.destination;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public long getSoTimeout() {
		return soTimeout;
	}

	public String getFilter() {
		return filter;
	}

	@Override
	public void destroy() throws Exception {
	}
	
	@Override
	public String toString() {
		if (this.beanName != null) {
			return this.beanName;
		}
		else {
			return super.toString();
		}
	}
}
