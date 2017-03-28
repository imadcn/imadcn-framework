package com.imadcn.test.canal;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CanalTest {

	static ClassPathXmlApplicationContext context;
	
	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		context.start();
	}
}
