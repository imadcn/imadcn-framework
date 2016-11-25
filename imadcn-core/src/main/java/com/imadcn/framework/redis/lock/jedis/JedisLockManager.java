package com.imadcn.framework.redis.lock.jedis;

import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import com.imadcn.framework.redis.config.JedisSentinelConfig;
import com.imadcn.framework.redis.lock.RedisLock;

public class JedisLockManager {
	
	private final UUID uuid = UUID.randomUUID();
	private Pool<Jedis> pool;
	private JedisPoolConfig poolConfig = new JedisPoolConfig();
	private JedisSentinelConfig sentinelConfig;
	
	public JedisLockManager(JedisSentinelConfig sentinelConfig) {
		this(null, sentinelConfig);
	}
	
	public JedisLockManager(JedisPoolConfig poolConfig, JedisSentinelConfig sentinelConfig) {
		this.poolConfig = poolConfig != null ? poolConfig : new JedisPoolConfig();
		this.sentinelConfig = sentinelConfig;
	}

	public RedisLock getLock(String key) {
		return new ReentrantJedisLock(getPool(), uuid, key);
	}
	
	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public void setSentinelConfig(JedisSentinelConfig sentinelConfig) {
		this.sentinelConfig = sentinelConfig;
	}

	protected Pool<Jedis> getPool() {
		if (pool == null) {
			synchronized (this) {
				if (pool == null) {
					if (sentinelConfig != null) {
						pool = createRedisSentinelPool();
					} else {
						pool = createRedisPool();
					}
				}
			}
		}
		return pool;
	}

	protected Pool<Jedis> createRedisSentinelPool() {
		return new JedisSentinelPool(sentinelConfig.getMasterName(), sentinelConfig.getSentinelNodes());
	}

	protected Pool<Jedis> createRedisPool() {
		return new JedisPool(poolConfig, sentinelConfig.getHost(), sentinelConfig.getPort());
	}
}
