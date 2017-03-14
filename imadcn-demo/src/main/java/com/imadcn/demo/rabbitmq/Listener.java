package com.imadcn.demo.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;

public class Listener implements MessageListener {
	
	private static Logger logger = LoggerFactory.getLogger(Listener.class);

	@Override
	public void onMessage(Message message) {
		try {
			MessageProperties properties = message.getMessageProperties();
			String exchange = properties.getReceivedExchange();
			String routingKey = properties.getReceivedRoutingKey();
			String type = properties.getType(); // 
			String content = new String(message.getBody(), properties.getContentEncoding());
			logger.info("R: exchange=[{}], routingKey=[{}], type=[{}], message=[{}]", exchange, routingKey, type, content);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
