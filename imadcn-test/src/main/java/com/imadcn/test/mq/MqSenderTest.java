package com.imadcn.test.mq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imadcn.demo.rabbitmq.Sender;

public class MqSenderTest {
	
	private ClassPathXmlApplicationContext context;
	private String configPath = "classpath:spring-config.xml";
	private Sender sender;
	
	public MqSenderTest() {
		context = new ClassPathXmlApplicationContext(new String[] { configPath });
		context.start();
		sender = context.getBean(Sender.class);
	}
	
	public static void main(String[] args) throws Exception {
		MqSenderTest t = new MqSenderTest();
		t.testSend();
	}
	
	public void testSend() {
		sender.send();
	}

}
