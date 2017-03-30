package com.imadcn.demo.kafka;

import org.springframework.kafka.core.KafkaTemplate;

import com.alibaba.fastjson.JSON;
import com.imadcn.framework.config.Heroes;
import com.imadcn.framework.config.Overwatch;

public class KafkaSender {
	
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void send() {
		for (int i = 0; i < 20; i++) {
			Overwatch ow = new Overwatch();
			ow.setId(i);
			ow.setName(Heroes.values()[i].toString());
			ow.setType(Heroes.values()[i]);
			kafkaTemplate.send("topic1", JSON.toJSONString(ow));
		}
	}
}
