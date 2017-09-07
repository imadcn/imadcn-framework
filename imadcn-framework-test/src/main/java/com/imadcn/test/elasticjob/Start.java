package com.imadcn.test.elasticjob;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
	
	private static ClassPathXmlApplicationContext context;
	private static String configPath = "classpath:spring-elasticjob.xml.xml";

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(new String[] {configPath});
		context.start();
	}

}
