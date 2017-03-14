package com.imadcn.test.mq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imadcn.demo.rabbitmq.Sender;

public class MqTest {
	
	private ClassPathXmlApplicationContext context;
	private String configPath = "classpath:spring-config.xml";
	private Sender sender;
	
	public MqTest() {
		context = new ClassPathXmlApplicationContext(new String[] { configPath });
		context.start();
		sender = context.getBean(Sender.class);
	}
	
	public static void main(String[] args) throws Exception {
		MqTest t = new MqTest();
		t.testSend();
	}
	
	public void testSend() {
		sender.send();
	}

}
