package com.imadcn.lock.test;

import java.util.Random;

public class SingletonTest {
	public static void main(String[] args) {
		Object a = null, b = null;
		boolean c = new Random().nextBoolean();
		if (c) {
			a = new Object();
		}
		b = a;
		System.out.println(b.hashCode());
	}
}