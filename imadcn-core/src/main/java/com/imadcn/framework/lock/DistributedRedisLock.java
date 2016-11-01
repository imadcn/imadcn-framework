package com.imadcn.framework.lock;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DistributedRedisLock implements Lock, Serializable {

	private static final long serialVersionUID = -5035167274324701059L;

	public void lock() {
		// TODO Auto-generated method stub

	}

	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	public void unlock() {
		// TODO Auto-generated method stub

	}

	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
