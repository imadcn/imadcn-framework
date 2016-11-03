package com.imadcn.framework.lock;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 基于Redis(客户端采用Jedis)的分布式锁
 * <br>redis版本<i> 2.6.12 </i>以上
 * @author imadcn
 */
public class JedisLock implements RedisLock, Serializable {
	
	private static final long serialVersionUID = -5035167274324701059L;
	private static Logger LOGGER = LoggerFactory.getLogger(JedisLock.class);
	
	private final String DEFAULT_LOCK_KEY = "distributed_lock";
	private final String dEFAULT_LOCK_VALUE = "redis_lock";
	private JedisPool jedisPool;
	
	public JedisLock(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void lock(String key, long waitTime, long leaseTime, TimeUnit unit) { // TODO Auto-generated method stub
		try {
			lockInterruptibly(key, waitTime, leaseTime, unit);
		} catch (Exception e) {
			
		}
	}

	public void lockInterruptibly(String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException { // TODO Auto-generated method stub
		/*try {
			long nanoTime = System.nanoTime();
			while(true) {
				Jedis jedis = jedisPool.getResource();
				String setReturn = jedis.set(key, LOCK_VALUE, "NX");
				if ("OK".equals(setReturn)) { // 锁定成功
					if (leaseTime >= 0) { // 设置锁定时间
						jedis.set(key, LOCK_VALUE, "XX", "PX", unit.toMillis(leaseTime));
					}
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
		}*/
		
		try {
			long nanoTime = System.nanoTime();
			while(true) {
				Jedis jedis = jedisPool.getResource();
				long setnx = jedis.setnx(key, dEFAULT_LOCK_VALUE);
				if (setnx == 1) { // 锁定成功
					if (leaseTime >= 0) { // 设置锁定时间
						jedis.pexpire(key, leaseTime);
					}
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
	}

	public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException { // TODO Auto-generated method stub
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
	
	public void unlock(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.del(key);
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
	
	public Condition newCondition() {
		throw new UnsupportedOperationException();
	}
	
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getId());
	}
}
