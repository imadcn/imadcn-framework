package com.imadcn.framework.canal.client;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.imadcn.framework.canal.config.ClusterCanalClientConfig;

/**
 * 集群模式的测试例子
 * 
 * @author jianghang 2013-4-15 下午04:19:20
 * @version 1.0.4
 */
public class ClusterCanalClient extends AbstractCanalClient implements InitializingBean {
	
	private ClusterCanalClientConfig clientConfig;

    public ClusterCanalClient(ClusterCanalClientConfig clientConfig) {
    	super(clientConfig.getDestination());
    	this.clientConfig = clientConfig;
    }

    @Override
	public void afterPropertiesSet() throws Exception {
    	String zkServers = clientConfig.getZkServers();
    	String destination = clientConfig.getDestination();
    	String username = clientConfig.getUsername();
    	String password = clientConfig.getPassword();
    	CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, destination, username, password);
    	setConnector(connector);
    	start();
    	/*Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    logger.info("## stop the canal client");
                    clientTest.stop();
                } catch (Throwable e) {
                    logger.warn("##something goes wrong when stopping canal:\n{}", ExceptionUtils.getFullStackTrace(e));
                } finally {
                    logger.info("## canal client is down.");
                }
            }

        });*/
	}
    
    public void stop() {
    	super.stop();
    }
}