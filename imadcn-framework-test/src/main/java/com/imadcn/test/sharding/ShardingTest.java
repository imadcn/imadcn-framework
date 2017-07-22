package com.imadcn.test.sharding;

import java.util.Date;
import java.util.Random;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.imadcn.framework.rdb.dao.ExamReportDao;
import com.imadcn.framework.rdb.dao.UserDao;
import com.imadcn.framework.rdb.domain.ExamReport;
import com.imadcn.framework.rdb.domain.User;
import com.imadcn.framework.util.DateFormatUtil;

public class ShardingTest {
	
	private static ClassPathXmlApplicationContext context;
	private static String configPath = "classpath:spring-config.xml";
	
	public ShardingTest() {
		context = new ClassPathXmlApplicationContext(new String[] { configPath });
		context.start();
	}
	
	public void doTest() throws Exception {
/*		ExamReportDao dao = context.getBean(ExamReportDao.class);
		Random random = new Random();
		for (int i = 0; i < 1000000; i++) {
			String orderId = DateFormatUtil.format(new Date(), "yyyyMMddHHmmssSSS") + String.format("%05d", random.nextInt(99999));
			ExamReport param = new ExamReport();
			param.setOrderId(orderId);
			dao.insert(param);
		}
		System.out.println("finished");*/
		new Run().start();
		new Run().start();
		new Run().start();
		new Run().start();
		new Run().start();
		new Run().start();
		new Run().start();
		new Run().start();
		new Run().start();
		new Run().start();
	}
	
	private class Run extends Thread {

		@Override
		public void run() {
			System.err.println("start in thread " + Thread.currentThread().getId());
			ExamReportDao dao = context.getBean(ExamReportDao.class);
			UserDao userDao = context.getBean(UserDao.class);
			Random random = new Random();
			String prefix = String.format("%05d", random.nextInt(99));
			String middle = DateFormatUtil.format(new Date(), "yyyyMMddHHmmssSSS");
			for (int i = 0; i < 1000; i++) {
				String orderId = prefix + middle + String.format("%08d", i);
				ExamReport param = new ExamReport();
				param.setOrderId(orderId);
				dao.insert(param);
				
				User user = new User();
				user.setName(orderId.substring(orderId.length() - 5));
				userDao.insert(user);
			}
			System.err.println("finished in thread " + Thread.currentThread().getId());
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		ShardingTest test = new ShardingTest();
		test.doTest();
	}

}
