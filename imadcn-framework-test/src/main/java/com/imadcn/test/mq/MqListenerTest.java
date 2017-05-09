package com.imadcn.test.mq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MqListenerTest {
	
	private ClassPathXmlApplicationContext context;
	private String configPath = "classpath:spring-config.xml";
	
	public MqListenerTest() {
		context = new ClassPathXmlApplicationContext(new String[] { configPath });
		context.start();
	}
	
	public static void main(String[] args) throws Exception {
		new MqListenerTest();
	}

}
