package com.imadcn.framework.redis.pubsub;

public interface PubSubEntry<E> {
	
	/**
	 * 释放资源
	 * @return
	 */
	public int release();
	
	/**
	 * 锁定资源
	 */
	public void acquire();
}
