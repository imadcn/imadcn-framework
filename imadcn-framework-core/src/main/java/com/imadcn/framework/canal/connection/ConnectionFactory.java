package com.imadcn.framework.canal.connection;

import com.alibaba.otter.canal.client.CanalConnector;

/**
 * Canal Connection Factory
 * @author imadcn
 * @since 1.0.0
 */
public interface ConnectionFactory {
	
	CanalConnector createCanalConnector();
	
	String getDestination();
	
	String getUsername();
	
	String getPassword();
}
