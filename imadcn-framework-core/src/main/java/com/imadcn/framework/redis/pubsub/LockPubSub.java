package com.imadcn.framework.redis.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imadcn.framework.redis.lock.spring.RedisLockEntry;

/**
 * 基于redis发布订阅功能，实现锁资源释放监听
 * @author imadcn
 */
public class LockPubSub extends PublishSubscribe<RedisLockEntry> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LockPubSub.class);

	/**
	 * default unlock message value
	 */
	public static final Long UNLOCK_PUBSUB_MESSAGE = 0L; // 

	@Override
	public void onMessage(RedisLockEntry value, Object message) {
		if (message.equals(UNLOCK_PUBSUB_MESSAGE + "")) {
			LOGGER.debug("[{}] released, latch id [{}]", value.getTag(), value.getLatch());
			value.getLatch().release(); // unlock
		}
	}

	@Override
	protected RedisLockEntry createEntry(String channelName) {
		return new RedisLockEntry(channelName);
	}
	
	public static void main(String[] args) {
		Object o = 111;
		System.out.println(o.equals(111));
	}
}
