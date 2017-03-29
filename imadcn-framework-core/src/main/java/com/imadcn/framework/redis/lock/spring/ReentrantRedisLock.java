package com.imadcn.framework.redis.lock.spring;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.imadcn.framework.redis.lock.RedisLock;
import com.imadcn.framework.redis.pubsub.LockPubSub;

/**
 * 可重入的分布式锁, 基于redis
 * @author imadcn
 */
public class ReentrantRedisLock implements RedisLock, Serializable  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReentrantRedisLock.class);

	private static final long serialVersionUID = -4746390442550034783L;
	private static final long DEFAULT_LEASE_TIME = 30 * 1000L; // 默认Redis锁，锁定时间，30秒
	private static final ConcurrentMap<String, Timer> RENEWAL_MAP = new ConcurrentHashMap<String, Timer>(); // 存放刷新锁生命期的task的map
	private static final LockPubSub PUBSUB = new LockPubSub(); // redis 发布订阅功能，用于lock排队解锁
	
	protected long internalLockLeaseTime = DEFAULT_LEASE_TIME; // 锁生命期
	
	private RedisTemplate<Object, Object> redisTemplate; // redisTemplate
	private RedisMessageListenerContainer container;
	
	final UUID uuid;
	final String groupName; // 功能组名字
	private String key; //rediskey Name;
	
	/**
	 * 可重入的分布式锁, 基于redis
	 * @param redisTemplate 
	 * @param container
	 * @param groupName 功能组名字
	 * @param key 需要锁定的唯一KEY名字
	 * @param uuid 线程uuid（鉴别同一key在不同物理机上lock/unlock触发事件）
	 */
	ReentrantRedisLock(RedisTemplate<Object, Object> redisTemplate, RedisMessageListenerContainer container, String groupName, String key, UUID uuid) {
		this.redisTemplate = redisTemplate;
		this.container = container;
		this.groupName = groupName;
		this.key = key;
		this.uuid = uuid;
	}
	
	@Override
	public void lock() {
		try {
			lockInterruptibly(DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Override
	public void lockInterruptibly() throws InterruptedException {
		lockInterruptibly(DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public boolean tryLock() {
		try {
			return tryLock(-1, DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return false;
	}
	
	@Override
	public boolean tryLock(long waitTime, TimeUnit unit) throws InterruptedException {
		try {
			return tryLock(waitTime, DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return false;
	}
	
	/**
	 * 可能会中断的锁定操作
	 * @param leaseTime 锁定周期
	 * @param unit 时间单位
	 * @throws InterruptedException
	 */
	protected void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException { 
		Long ttl = tryAcquire(leaseTime, unit); // 尝试锁定，成功返回null，已被占用则返回被占用资源剩下锁定时间
		if (ttl == null) { // lock acquired
			return;
		}
		RedisLockEntry subscribeEntry = subscribe(); // 添加pubsub功能，监听解锁时间
		try {
			while(true) {
				ttl = tryAcquire(leaseTime, unit);
				if (ttl == null) { // lock acquired
					break;
				}
				RedisLockEntry entry = PUBSUB.getEntry(getEntryName()); // 用于触发解锁事件操作的实例
				LOGGER.debug("[{}] get entry", entry.getTag());
				if (ttl >= 0) {
					LOGGER.debug("[{}] acquire with tll {} ms, latch id [{}]", entry.getTag(), ttl, entry.getLatch());
					entry.getLatch().tryAcquire(ttl, TimeUnit.MILLISECONDS); // 基于信号量，锁定操作
				} else {
					LOGGER.debug("[{}] acquire without tlls, latch id [{}]", entry.getTag(), entry.getLatch());
					entry.getLatch().acquire(); 
				}
				LOGGER.debug("await {} ms, and ended by {}", ttl, entry.getTag());
			}
		} finally {
			unsubscribe(subscribeEntry); // 等他他人解锁，切自己锁定成功，取消监听解锁事件
		}
	}
	
	/**
	 * 尝试锁定，如果获取到锁则立马返回true，否则持续等待，直至等待超时，并返回false
	 * @param waitTime 等待时间
	 * @param leaseTime 锁定周期
	 * @param unit 时间单位
	 * @return 锁定成功返回<b>true</b>，超时返回<b>false</b>
	 * @throws InterruptedException
	 */
	protected boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
		Long ttl = tryAcquire(leaseTime, unit);
		if (ttl == null) { // lock acquired
			return true;
		}
		long time = unit.toMillis(waitTime);
		if (time <= 0) {
			return false;
		}
		RedisLockEntry subscribeEntry = subscribe();
		try {
			while(true) {
				ttl = tryAcquire(leaseTime, unit);
				if (ttl == null) { // lock acquired
					return true;
				}
				if (time <= 0) {
					return false;
				}
				
				// waiting for message
                long current = System.currentTimeMillis();
                RedisLockEntry entry = PUBSUB.getEntry(getEntryName());
                
                if (ttl >= 0 && ttl < time) { // 前一资源剩余生命周期 和 自己等待时间，以最短的作为锁定时间
                    entry.getLatch().tryAcquire(ttl, TimeUnit.MILLISECONDS);
                } else {
                    entry.getLatch().tryAcquire(time, TimeUnit.MILLISECONDS);
                }

                long elapsed = System.currentTimeMillis() - current;
                time -= elapsed;
			}
		} finally {
			unsubscribe(subscribeEntry);
		}
		
	}

	@Override
	public void unlock() { 
		Long eval = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				Long eval = connection.eval(serializeScript(UNLOCK_SCRIPT), ReturnType.INTEGER, 2, serializeParams(getRedisKey(), getChannelName(), getHashField(), internalLockLeaseTime, LockPubSub.UNLOCK_PUBSUB_MESSAGE));
				return eval;
			}
		});
		if (eval == null) { // 解锁不是被自己锁定的资源时，抛出异常
			throw new IllegalMonitorStateException("attempt to unlock lock, not locked by current thread by node id: " + uuid + " thread-id: " + Thread.currentThread().getId());
		}
		if (eval == 1) { // 解锁成功，取消生命周期时常刷新task
			cancelExpirationRenewal();
		}
	}
	
	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isLocked() { 
		Boolean execute = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				Long eval = connection.eval(serializeScript(CHECK_LOCKED_SCRIPT), ReturnType.INTEGER, 1, serializeParams(getRedisKey(), getHashField()));
				if (eval == 1) { 
					return Boolean.TRUE;
				} else {
					return Boolean.FALSE;
				}
			}
		});
		return execute;
	}

	/**
	 * 尝试锁定某个key
	 * @param leaseTime 生命周期
	 * @param unit 时间单位
	 * @return
	 */
	private Long tryAcquire(long leaseTime, final TimeUnit unit) { 
		if (leaseTime >= 0) {
			this.internalLockLeaseTime = unit.toMillis(leaseTime);
		}
		Long eval = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				Long eval = connection.eval(serializeScript(LOCK_SCRIPT), ReturnType.INTEGER, 1, serializeParams(getRedisKey(), getHashField(), internalLockLeaseTime));
				return eval;
			}
		});
		if (eval == null) { 
			/**
			 * 锁定成功，在脚本中，添加了超时机制，防止某个机器宕机，造成资源死锁，因为有设置超时时间
			 * 但是，有可能任务lock和unlock之间的任务还没执行完，但超时已到，被其他线程锁定的情况，因为设置了1/3超时时间自动刷新生命周期的情况
			 */
			scheduleExpirationRenewal();
		} 
		return eval;
	}
	
	/**
	 * 定时刷新过期时间任务
	 */
	private void scheduleExpirationRenewal() {
		if (RENEWAL_MAP.containsKey(getRedisKey())) {
            return;
        }
		Timer timer = new Timer();
		long delay = internalLockLeaseTime / 3;
		long period = internalLockLeaseTime / 3;
		period = period > 0 ? period : 1;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				redisTemplate.execute(new RedisCallback<Long>() {
					public Long doInRedis(RedisConnection connection) throws DataAccessException {
						Long pttl = connection.eval(serializeScript(RENEWAL_SCRIPT), ReturnType.INTEGER, 1, serializeParams(getRedisKey(), internalLockLeaseTime));
						return pttl; // time to expired
					}
				});
			}
		}, delay, period);
		LOGGER.debug("scheduled expiration renewal task in {}", getRedisKey());
        if (RENEWAL_MAP.putIfAbsent(getRedisKey(), timer) != null) {
        	timer.cancel();
        }
	}
	
	/**
	 * 取消刷新过期时间 
	 */
	private void cancelExpirationRenewal() {
		LOGGER.debug("cancel expiration renewal task in {}", getRedisKey());
		Timer timer = RENEWAL_MAP.remove(getRedisKey());
        if (timer != null) {
        	timer.cancel();
        }
        LOGGER.debug("renewal task size : {}", ReentrantRedisLock.RENEWAL_MAP.size());
	}

	/**
	 * 添加解锁监听事件
	 * @return
	 */
	private RedisLockEntry subscribe() { 
		return PUBSUB.subscribe(getEntryName(), getChannelName(), container);
	}
	
	/**
	 * 解除监听
	 * @param subscribeEntry
	 */
	private void unsubscribe(RedisLockEntry subscribeEntry) { 
		PUBSUB.unsubscribe(subscribeEntry, getEntryName(), container);
	}
	
	private String getRedisKey() { // redis中锁key
		// prefix + groupname + keyname
		return "redis_lock_key." + groupName + "." + key; 
	} 
	
	private String getChannelName() { // pubsub监听频道id
		return "redis_lock_channel." + getRedisKey();
	}
	
	private String getHashField() { // redis存储采用hash， 其中一个hashkey为 uuid+线程id，确保一个线程只能被自己解锁，不能被其他线程解锁
		return uuid + ":" + Thread.currentThread().getId();
	}
	
	private String getEntryName() { // 锁定资源ID
		return uuid + ":" + getRedisKey() + Thread.currentThread().getId();
	}
	
	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@SuppressWarnings("unchecked")
	protected RedisSerializer<Object> getKeySerializer() {
		return (RedisSerializer<Object>) redisTemplate.getKeySerializer();
	}
	
	@SuppressWarnings("unchecked")
	protected RedisSerializer<Object> getValueSerializer() {
		return (RedisSerializer<Object>) redisTemplate.getValueSerializer();
	}
	
	/**
	 * 序列化KEY和参数
	 * @param params
	 * @return
	 */
	protected byte[][] serializeParams(Object... params) {
		byte[][] serialize = null;
		if (params != null && params.length > 0) {
			serialize = new byte[params.length][];
			for (int i = 0; i < params.length; i++) {
				serialize[i] = getKeySerializer().serialize(params[i] + "");
			}
		}
		return serialize;
	}
	
	/**
	 * 序列化lua脚本
	 * @param params
	 * @return
	 */
	protected byte[] serializeScript(Object params) {
		return getKeySerializer().serialize(params);
	}
	
	/**
	 * unlock script
	 */
	private static final String UNLOCK_SCRIPT = 
										 "if (redis.call('exists', KEYS[1]) == 0) then " +
											 "redis.call('publish', KEYS[2], ARGV[3]); " +
									         "return 1; " +
									     "end;" +
									     "if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then " +
									         "return nil;" +
									     "end; " +
									     "local counter = redis.call('hincrby', KEYS[1], ARGV[1], -1); " +
									     "if (counter > 0) then " +
									         "redis.call('pexpire', KEYS[1], ARGV[2]); " +
									         "return 0; " +
									     "else " +
									         "redis.call('del', KEYS[1]); " +
									         "redis.call('publish', KEYS[2], ARGV[3]); " +
									         "return 1; "+
									     "end; " +
									     "return nil;";
	
	/**
	 * lock script
	 */
	private static final String LOCK_SCRIPT = 
									   "if (redis.call('exists', KEYS[1]) == 0) then " +
								           "redis.call('hset', KEYS[1], ARGV[1], 1); " +
								           "redis.call('pexpire', KEYS[1], ARGV[2]); " +
								           "return nil; " +
								       "end; " +
								       "if (redis.call('hexists', KEYS[1], ARGV[1]) == 1) then " +
								           "redis.call('hincrby', KEYS[1], ARGV[1], 1); " +
								           "redis.call('pexpire', KEYS[1], ARGV[2]); " +
								           "return nil; " +
								       "end; " +
								       "return redis.call('pttl', KEYS[1]);";
	
	/**
	 * check whether the lock is locked by any thread
	 */
	private static final String CHECK_LOCKED_SCRIPT = 
										"if (redis.call('exists', KEYS[1]) == 0) then " +
									        "return 1; " + 
									    "else " + 
									        "return 0; "+
									    "end;";
	
	/**
	 * renewal the expiration
	 */
	private static final String RENEWAL_SCRIPT = 
										"redis.call('pexpire', KEYS[1], ARGV[1]); " + 
										"return redis.call('pttl', KEYS[1]);";
	
}
