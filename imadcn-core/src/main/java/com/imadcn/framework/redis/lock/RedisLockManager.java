package com.imadcn.framework.redis.lock;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class RedisLockManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockManager.class);
	private final UUID uuid = UUID.randomUUID();
	
	private RedisTemplate<Object, Object> redisTemplate;
	private RedisMessageListenerContainer container;
	private MessageListener messageListener;
	
	public RedisLockManager() {}
	
	public RedisLockManager(RedisTemplate<Object, Object> redisTemplate, RedisMessageListenerContainer container) {
		this.redisTemplate = redisTemplate;
		this.container = container;
		addDaemonMessageListener();
	}

	/**
	 * 获取Redis锁
	 * @param key
	 * @return
	 */
	public ReentrantRedisLock getLock(String key) {
		return new ReentrantRedisLock(redisTemplate, container, key, uuid);
	}
	
	private void addDaemonMessageListener() {
		if (messageListener == null) {
			messageListener = new MessageListener() {
				@Override
				public void onMessage(Message message, byte[] pattern) {
					LOGGER.warn("daemon listener received some message, that's strange");
				}
			};
			container.addMessageListener(messageListener, new ChannelTopic("__redis_lock_channel__:daemon"));
		}
	}

	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setContainer(RedisMessageListenerContainer container) {
		this.container = container;
		addDaemonMessageListener();
	}
}
