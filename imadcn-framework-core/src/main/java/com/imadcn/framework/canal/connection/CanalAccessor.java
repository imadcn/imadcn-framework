package com.imadcn.framework.canal.connection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.alibaba.otter.canal.client.CanalConnector;

public abstract class CanalAccessor implements InitializingBean {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private volatile ConnectionFactory connectionFactory;

	/**
	 * Set the ConnectionFactory to use for obtaining Canal
	 *
	 * @param connectionFactory The connection factory.
	 */
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * @return The ConnectionFactory that this accessor uses for obtaining Canal
	 */
	public ConnectionFactory getConnectionFactory() {
		return this.connectionFactory;
	}
	
	protected CanalConnector createCanalConnector() throws IOException {
		return this.connectionFactory.createCanalConnector();
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.connectionFactory, "ConnectionFactory is required");
	}
}
