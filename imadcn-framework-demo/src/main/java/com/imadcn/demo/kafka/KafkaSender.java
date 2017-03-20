package com.imadcn.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.imadcn.framework.config.Heroes;
import com.imadcn.framework.config.Overwatch;

@Component
public class KafkaSender {
	
	@Autowired
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
