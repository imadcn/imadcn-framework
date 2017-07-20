package com.imadcn.test.sharding;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imadcn.framework.rdb.dao.ExamReportDao;
import com.imadcn.framework.rdb.domain.ExamReport;

public class ShardingTest {
	
	private static ClassPathXmlApplicationContext context;
	private static String configPath = "classpath:spring-config.xml";
	
	public ShardingTest() {
		context = new ClassPathXmlApplicationContext(new String[] { configPath });
		context.start();
	}
	
	public void doTest() throws Exception {
		ExamReportDao dao = context.getBean(ExamReportDao.class);
		for (int i = 0; i < 1000; i++) {
			Thread.sleep(100);
			String orderId = String.valueOf(System.currentTimeMillis());
			ExamReport param = new ExamReport();
			param.setOrderId(orderId);
			dao.insert(param);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ShardingTest test = new ShardingTest();
		test.doTest();
	}

}
