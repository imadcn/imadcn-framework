package com.imadcn.framework.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DdSharding {
	
	private ClassPathXmlApplicationContext context;
	
	public DdSharding() {
		context = new ClassPathXmlApplicationContext("classpath:*.xml");
		context.start();
	}

	public static void main(String[] args) {
		DdSharding test = new DdSharding();
	}
}
