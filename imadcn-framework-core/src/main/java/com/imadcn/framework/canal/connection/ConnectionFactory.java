package com.imadcn.framework.canal.connection;

/**
 * Canal Connection Factory
 * @author imadcn
 * @since 1.0.0
 */
public interface ConnectionFactory {
	
	String getDestination();
	
	String getUsername();
	
	String getPassword();
	
	long getSoTimeout();
	
	String getFilter();
}
