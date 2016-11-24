package com.imadcn.framework.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于redis发布订阅功能，实现锁资源释放监听
 * @author imadcn
 */
public class LockPubSub extends PublishSubscribe<LockEntry> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LockPubSub.class);

	/**
	 * default unlock message value
	 */
	public static final Long UNLOCK_PUBSUB_MESSAGE = 0L; // 

	@Override
	public void onMessage(LockEntry value, Object message) {
		if (message.equals(UNLOCK_PUBSUB_MESSAGE + "")) {
			LOGGER.debug("[{}] released, latch id [{}]", value.getTag(), value.getLatch());
			value.getLatch().release(); // unlock
		}
	}

	@Override
	protected LockEntry createEntry(String channelName) {
		return new LockEntry(channelName);
	}
}
