package com.imadcn.demo.kafka;

import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

public class KafkaDemoListener implements MessageListener<String, String> {
	
	private static Logger logger = LoggerFactory.getLogger(KafkaDemoListener.class);

	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
		long threadId = Thread.currentThread().getId();
		String topic = data.topic();
		String value = data.value();
		int partition = data.partition();
		String key = data.key();
		long sleepTime = new Random().nextInt(2000);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		logger.info("[kafka] received message, thread[{}], sleep {} ms| key: {}, partition: {}, topic: {}, value: {}", threadId, sleepTime, key, partition, topic, value);
	}
}
