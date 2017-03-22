package com.imadcn.framework.canal.client;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.imadcn.framework.canal.config.SimpleCanalClientConfig;

/**
 * 单机模式的测试例子
 * 
 * @author jianghang 2013-4-15 下午04:19:20
 * @version 1.0.4
 */
public class SimpleCanalClient extends AbstractCanalClient implements InitializingBean {
	
	private SimpleCanalClientConfig clientConfig;

    public SimpleCanalClient(SimpleCanalClientConfig clientConfig){
        super(clientConfig.getDestination());
        this.clientConfig = clientConfig;
    }
    
    @Override
	public void afterPropertiesSet() throws Exception {
    	String host = clientConfig.getHost();
    	int port = clientConfig.getPort();
    	String destination = clientConfig.getDestination();
    	String username = clientConfig.getUsername();
    	String password = clientConfig.getPassword();
    	CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(host, port), destination, username, password);
        setConnector(connector);
        start();
	}
}