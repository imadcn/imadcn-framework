package com.imadcn.test.schema;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imadcn.framework.config.Overwatch;

public class SchemaTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		context.start();
		Overwatch overwatch = (Overwatch) context.getBean("1001");
		System.out.println(overwatch);
		context.close();
	}
}
