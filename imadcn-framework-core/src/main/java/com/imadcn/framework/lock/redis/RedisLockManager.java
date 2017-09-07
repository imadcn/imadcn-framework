package com.imadcn.framework.lock.redis;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Redis lock 配置管理器
 * @author yc
 * @since 2017年1月16日
 */
public class RedisLockManager implements InitializingBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockManager.class);
	private final UUID uuid = UUID.randomUUID();
	
	protected String id;
	@Autowired(required = false)
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired(required = false)
	private RedisMessageListenerContainer container;
	private volatile MessageListener messageListener;
	private String groupName; // 功能组名
	
	public RedisLockManager() {}
	
	/**
	 * Redis lock 配置管理器
	 * @param redisTemplate
	 * @param container 
	 * @param groupName 功能组名(在不同程序中，KEY可能相同，需加入鉴别功能模块的组名)
	 */
	public RedisLockManager(RedisTemplate<Object, Object> redisTemplate, RedisMessageListenerContainer container, String groupName) {
		this.redisTemplate = redisTemplate;
		this.container = container;
		this.groupName = groupName;
		addDaemonMessageListener();
	}

	/**
	 * 获取Redis锁
	 * @param key
	 * @return
	 */
	public RedisLock getLock(String key) {
		return new ReentrantRedisLock(redisTemplate, container, groupName, key, uuid);
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

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (redisTemplate == null) {
			throw new IllegalArgumentException("redis template cant be null");
		}
		if (container == null) {
			throw new IllegalArgumentException("redis message listener cant be null");
		}
		if (groupName == null || groupName.isEmpty()) {
			throw new IllegalArgumentException("lock group name cant be null or empty");
		}
		addDaemonMessageListener();	
	}
}
