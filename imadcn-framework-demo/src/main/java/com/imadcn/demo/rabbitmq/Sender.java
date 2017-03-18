package com.imadcn.demo.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.alibaba.fastjson.JSON;
import com.imadcn.framework.config.Heroes;
import com.imadcn.framework.config.Overwatch;

public class Sender {

	private RabbitTemplate rabbitTemplate;
	private String exchange;
	private String routingKey;

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public void send() {
		Overwatch overwatch = new Overwatch();
		overwatch.setId(1001);
		overwatch.setName("GENJI");
		overwatch.setType(Heroes.Genji);

		String json = JSON.toJSONString(overwatch);
		rabbitTemplate.convertAndSend(exchange, routingKey, json);
	}

}
