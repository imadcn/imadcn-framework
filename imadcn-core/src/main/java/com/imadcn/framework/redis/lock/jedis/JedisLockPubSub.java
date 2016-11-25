package com.imadcn.framework.redis.lock.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JedisLockPubSub extends JedisPublishSubscribe<JedisLockEntry> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisLockPubSub.class);
	/**
	 * default unlock message value
	 */
	public static final Long UNLOCK_PUBSUB_MESSAGE = 0L; // 

	@Override
	protected JedisLockEntry createEntry(String channelName) {
		return new JedisLockEntry(channelName);
	}

	@Override
	protected void onMessage(JedisLockEntry value, Object message) {
		if (message.equals(UNLOCK_PUBSUB_MESSAGE + "")) {
			LOGGER.debug("[{}] released, latch id [{}]", value.getTag(), value.getLatch());
			value.getLatch().release(); // unlock
		}
	}

}
