package com.imadcn.framework.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 基于 {@link java.util.concurrent.locks.Lock} 和Redis的分布式锁的实现
 * @author imadcn
 */
public interface RedisLock extends Lock {
	
	/**
	 * 加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 最长等待时间。
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param unit 参数 {@code waitTime} 的时间单位
	 */
	public void lock(long waitTime, TimeUnit unit);
	
	/**
	 * 加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 时间<br>加锁成功后，会一直锁定直到 {@code unlock} 方法被调用或者持续至 {@code leaseTime} 最长锁定时间。
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param leaseTime 加锁成功后的最长锁定时间，如果为-1，为一直锁定直至被明确告知要求解锁。
	 * @param unit 参数 {@code waitTime} 和 {@code leaseTime} 的时间单位
	 */
	public void lock(long waitTime, long leaseTime, TimeUnit unit);
	
	/**
	 * 加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 时间<br>加锁成功后，会一直锁定直到 {@code unlock} 方法被调用或者持续至 {@code leaseTime} 最长锁定时间。
	 * @param key 锁定关键字
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param leaseTime 加锁成功后的最长锁定时间，如果为-1，为一直锁定直至被明确告知要求解锁。
	 * @param unit 参数 {@code waitTime} 和 {@code leaseTime} 的时间单位
	 */
	public void lock(String key, long waitTime, long leaseTime, TimeUnit unit);
	
	/**
	 * 加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 最长等待时间。
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param unit 参数 {@code waitTime} 的时间单位
	 * @throws InterruptedException
	 */
	public void lockInterruptibly(long waitTime, TimeUnit unit) throws InterruptedException;
	
	/**
	 * 加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 时间<br>加锁成功后，会一直锁定直到 {@code unlock} 方法被调用或者持续至 {@code leaseTime} 最长锁定时间。
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param leaseTime 加锁成功后的最长锁定时间，如果为-1，为一直锁定直至被明确告知要求解锁。
	 * @param unit 参数 {@code waitTime} 和 {@code leaseTime} 的时间单位
	 * @throws InterruptedException
	 */
	public void lockInterruptibly(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
	
	/**
	 * 加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 时间<br>加锁成功后，会一直锁定直到 {@code unlock} 方法被调用或者持续至 {@code leaseTime} 最长锁定时间。
	 * @param key 锁定关键字
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param leaseTime 加锁成功后的最长锁定时间，如果为-1，为一直锁定直至被明确告知要求解锁。
	 * @param unit 参数 {@code waitTime} 和 {@code leaseTime} 的时间单位
	 * @throws InterruptedException
	 */
	public void lockInterruptibly(String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
	
	/**
	 * 尝试加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 时间<br>加锁成功后，会一直锁定直到 {@code unlock} 方法被调用或者持续至 {@code leaseTime} 最长锁定时间。
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param leaseTime 加锁成功后的最长锁定时间，如果为-1，为一直锁定直至被明确告知要求解锁。
	 * @param unit 参数 {@code waitTime} 和 {@code leaseTime} 的时间单位
	 * @return 锁定成功后<b> 立即 </b>返回 {@code true}，否则返回 {@code false}
	 */
	public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
	
	/**
	 * 尝试加锁
	 * <p> 如果未能加锁成功，则一直等待持续至 {@code waitTime} 时间<br>加锁成功后，会一直锁定直到 {@code unlock} 方法被调用或者持续至 {@code leaseTime} 最长锁定时间。
	 * @param key 锁定关键字
	 * @param waitTime 加锁成功前的最长等待时间。如果为-1，则一直等待。
	 * @param leaseTime 加锁成功后的最长锁定时间，如果为-1，为一直锁定直至被明确告知要求解锁。
	 * @param unit 参数 {@code waitTime} 和 {@code leaseTime} 的时间单位
	 * @return 锁定成功后<b> 立即 </b>返回 {@code true}，否则返回 {@code false}
	 */
	public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;
	
	/**
	 * 解锁
	 * @param key 锁定关键字
	 */
	public void unlock(String key);
}
