package com.imadcn.lock.test;

import java.util.Random;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RLock;

public class RedissonLockTest {
	
	RedissonClient redissonClient;
	
	public RedissonLockTest() {
		Config config = new Config();
		config.useSentinelServers().addSentinelAddress("10.2.50.36:26579", "10.2.50.37:26479", "10.2.50.38:26379").setMasterName("mymaster");
		redissonClient = Redisson.create(config);
		
	}
	
	public void test() {
		try {
			RLock lock = redissonClient.getLock("redis_lock.2016120100000000000000001");
			lock.lock();
			if (new Random().nextInt(100) % 2 == 0) {
				throw new RuntimeException("exception in clock test.");
			}
			lock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		final RedissonLockTest test = new RedissonLockTest();
		for (int i = 0; i < 10; i++) {
			new Thread(){
				@Override
				public void run() {
					test.test();
				}
			}.start();
		}
	}
}
