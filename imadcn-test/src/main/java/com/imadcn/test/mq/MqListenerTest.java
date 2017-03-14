package com.imadcn.test.mq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imadcn.demo.rabbitmq.Sender;

public class MqListenerTest {
	
	private ClassPathXmlApplicationContext context;
	private String configPath = "classpath:spring-config.xml";
	protected Sender sender;
	
	public MqListenerTest() {
		context = new ClassPathXmlApplicationContext(new String[] { configPath });
		context.start();
		sender = context.getBean(Sender.class);
	}
	
	public static void main(String[] args) throws Exception {
		new MqListenerTest();
	}

}
