package com.imadcn.test.kafka;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imadcn.demo.kafka.KafkaSender;

public class KafkaTest {

	static ClassPathXmlApplicationContext context;
	
	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		context.start();
		KafkaSender sender = context.getBean(KafkaSender.class);
		sender.send();
	}
}
