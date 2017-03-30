package com.imadcn.test.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.imadcn.framework.lock.redis.RedisLock;
import com.imadcn.framework.lock.redis.RedisLockManager;
import com.imadcn.framework.util.UIDUtil;

public class RedisLockTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockTest.class);
	
	private static ClassPathXmlApplicationContext context;
	private static String configPath = "classpath:spring-config.xml";
	private static RedisLockManager redisLockManager;
	private static ThreadPoolTaskExecutor executor;
	
	public RedisLockTest() {
		context = new ClassPathXmlApplicationContext(new String[] { configPath });
		context.start();
		executor = context.getBean(ThreadPoolTaskExecutor.class);

		redisLockManager = context.getBean(RedisLockManager.class);
	}
	
	/*public void test1() {
		DynamicRedisSource redisTemplate = context.getBean(DynamicRedisSource.class);
		final String key = "redis.distributed.lock." + System.currentTimeMillis();
		System.err.println(key);
		SeReetrantRedisLock lock = new SeReetrantRedisLock(redisTemplate, UUID.randomUUID());
		lock.lock(key, 1 * 60 * 1000, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
		System.err.println("lock_successful");
		lock.unlock(key + System.currentTimeMillis());
	}*/
	
	private Integer counter = 0;
	private static List<String> queue = new ArrayList<String>();
	private static List<Exception> exceptions = new ArrayList<Exception>();
	
	private static final int typeNum = 5;
	private static final int execNum = 10;
	
	public void test1() {
		for (int i = 0; i < typeNum; i++) {
			final String key = UIDUtil.noneDashUuid();
			for (int j = 0; j < execNum; j++) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						test1_1(key);
					}
				});
			}
		}
	}
	
	public void test2() {
		final String key = UIDUtil.noneDashUuid();
		for (int i = 0; i < 10; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					test2_1(key);
				}
			});
		}
	}
	
	public void test3() {
		// test in different process
//		String key = UIDUtil.noneDashUuid();
		String key = "2017010100000000000001";
		RedisLock lock = redisLockManager.getLock(key);
		long threadId = Thread.currentThread().getId();
		try {
			lock.lock();
			long timestmap = System.currentTimeMillis();
			LOGGER.info("lock in thread [{}] with key [{}]", threadId, key);
			while(true) {
				if (System.currentTimeMillis() - timestmap >= 10 * 1000) {
					break;
				}
			}
		} finally {
			LOGGER.info("key [{}] unlock in thread [{}]", key, threadId);
			lock.unlock();
		}
	}
	
	public void test4() {
		final String key = UIDUtil.noneDashUuid();
		for (int j = 0; j < execNum; j++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					test1_1(key);
				}
			});
		}
	} 

	private void test1_1(String key) {
		try {
			long threadId = Thread.currentThread().getId();
			// print(key);
			RedisLock lock = redisLockManager.getLock(key);
			lock.lock();
			long sleepElapse = new Random().nextInt(5000);
			queue.add(key + ":" + threadId);
			LOGGER.info(String.format("key [%s] locked in thread id [%s]. try to sleep [%s] ms", key, threadId, sleepElapse));
			long beginTime = System.currentTimeMillis();
			while(true) {
				if (beginTime + sleepElapse < System.currentTimeMillis()) {
					break;
				}
			}
			lock.unlock();
			synchronized (counter) {
				counter++;
			}
			LOGGER.info(String.format("key [%s] unlocked in thread id [%s].", key, threadId));
		} catch (Exception e) {
			e.printStackTrace();
			exceptions.add(e);
		}
		showResult();
	}
	
	private void test2_1(String key) {
		try {
			// print(key);
			RedisLock lock = redisLockManager.getLock(key);
			lock.tryLock(1, TimeUnit.MILLISECONDS);
			long sleepElapse = 0; //50 * 60 * 1000 + new Random().nextInt(500);
			long threadId = Thread.currentThread().getId();
			queue.add(key + ":" + threadId);
			LOGGER.info(String.format("key [%s] locked in thread id [%s]. try to sleep [%s] ms", key, threadId, sleepElapse));
			long beginTime = System.currentTimeMillis();
			while(true) {
				if (beginTime + sleepElapse < System.currentTimeMillis()) {
					break;
				}
			}
			lock.unlock();
			synchronized (counter) {
				counter++;
			}
			LOGGER.info(String.format("key [%s] unlocked in thread id [%s].", key, threadId));
		} catch (Exception e) {
			e.printStackTrace();
			exceptions.add(e);
		}
		showResult();
	}
	
	private void showResult() {
		System.out.println("counter: " + counter);
		if (counter >= typeNum * execNum && typeNum > 0 && execNum > 0) {
			try {
				Thread.sleep(2000);
				System.out.println("total count : " + counter);
				System.err.println("-------------------------------------------------------------------------");
				if (queue.size() <= 500) {
					for (int i = 0; i < queue.size(); i++) {
						if (i % 2 == 0) {
							Thread.sleep(2);
							System.err.println(queue.get(i));
						} else {
							Thread.sleep(2);
							System.out.println(queue.get(i));
						}
					}
				} else {
					System.out.println("too many results, show total only when it's greater than 500 : [" + queue.size() + "]");
				}
				System.out.println("-------------------------------------------------------------------------");
				if (exceptions.size() <= 500) {
					for (int i = 0; i < exceptions.size(); i++) {
						Exception e = exceptions.get(i);
						if (i % 2 == 0) {
							Thread.sleep(2);
							System.err.println(e);
						} else {
							Thread.sleep(2);
							System.out.println(e);
						}
					}
				} else {
					System.out.println("too many exceptions, only show total when greater than 500 : [" + exceptions.size() + "]");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	void print(Object o) {
		System.err.println(o);
	} 
	
	public static void main(String[] args) throws Exception {
		RedisLockTest t = new RedisLockTest();
		t.test1();
	}
}
