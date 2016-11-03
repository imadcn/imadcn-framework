package com.imadcn.framework.lock;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.redisson.client.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class SpringJedisLock implements RedisLock, Serializable  {

	private static final long serialVersionUID = -4746390442550034783L;
	private static Logger LOGGER = LoggerFactory.getLogger(SpringJedisLock.class);

	private final String DEFAULT_LOCK_KEY = "distributed_lock";
	private final String dEFAULT_LOCK_VALUE = "redis_lock";
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	/**
	 * Set the expiry for the given key if its value matches the supplied value.
	 * Returns 1 on success, 0 on failure setting expiry or key not existing, -1 if the key value didn't match
	 */
	private final String EXTEND_IF_MATCHING_VALUE_SCRIPT  = 
									  "local currentVal = redis.call('get', KEYS[1])" + 
									  "if (currentVal == false) then" + 
									  "		return redis.call('set', KEYS[1], ARGV[1], 'PX', ARGV[2]) and 1 or 0" +
									  "elseif (currentVal == ARGV[1]) then" + 
									  "		return redis.call('pexpire', KEYS[1], ARGV[2])" +
									  "else" +
									  "		return -1" + 
									  "end";
	
	public SpringJedisLock(RedisTemplate<Serializable, Serializable> redisTemplate, UUID uuid) {
		this.redisTemplate = redisTemplate;
	}

	public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException { // TODO
		try {
			long nanoTime = System.nanoTime();
			while(true) {
				Jedis jedis = jedisPool.getResource();
				long setnx = jedis.setnx(key, dEFAULT_LOCK_VALUE);
				if (setnx == 1) { // 锁定成功
					if (leaseTime >= 0) { // 设置锁定时间
						jedis.pexpire(key, leaseTime);
					}
					return true;
				} else { // 已经存在锁了，
					if (waitTime >= 0) { // 等待时间
						if (System.nanoTime() - nanoTime > unit.toNanos(waitTime)) {
							break;
						}
					}
				}
			}
		} catch (JedisConnectionException e) {
			LOGGER.error("", e);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return false;
	}
	
	public void unlock(String key) { // XXX

	}
	
	public void lock(String key, long waitTime, long leaseTime, TimeUnit unit) {
		try {
			lockInterruptibly(key, waitTime, leaseTime, unit);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public void lockInterruptibly(String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
		tryLock(key, waitTime, leaseTime, unit);
	}

	public void lock() {
		lock(DEFAULT_LOCK_KEY, -1, -1, TimeUnit.MILLISECONDS);
	}

	public void lock(long waitTime, TimeUnit unit) {
		lock(DEFAULT_LOCK_KEY, waitTime, -1, TimeUnit.MILLISECONDS);
	}

	public void lock(long waitTime, long leaseTime, TimeUnit unit) {
		lock(DEFAULT_LOCK_KEY, waitTime, leaseTime, TimeUnit.MILLISECONDS);
	}
	
	public void lockInterruptibly() throws InterruptedException {
		lockInterruptibly(DEFAULT_LOCK_KEY, -1, -1, TimeUnit.MILLISECONDS);
	}

	public void lockInterruptibly(long waitTime, TimeUnit unit) throws InterruptedException {
		lockInterruptibly(DEFAULT_LOCK_KEY, waitTime, -1, TimeUnit.MILLISECONDS);
	}

	public void lockInterruptibly(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
		lockInterruptibly(DEFAULT_LOCK_KEY, waitTime, leaseTime, TimeUnit.MILLISECONDS);
	}

	public boolean tryLock() {
		try {
			return tryLock(DEFAULT_LOCK_KEY, -1, -1, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			 Thread.currentThread().interrupt();
		}
		return false;
	}

	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return tryLock(DEFAULT_LOCK_KEY, time, -1, unit);
	}

	public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
		return tryLock(DEFAULT_LOCK_KEY, waitTime, leaseTime, unit);
	}
	
	public void unlock() {
		unlock(DEFAULT_LOCK_KEY);
	}
	
	public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Condition newCondition() {
		throw new UnsupportedOperationException();
	}
	
}
