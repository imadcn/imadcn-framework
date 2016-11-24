package com.imadcn.framework.locks;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class RedisMessageListenerContainerUltra extends RedisMessageListenerContainer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageListenerContainerUltra.class);
	
	public boolean isListening(){
		try {
			Field[] fields = this.getClass().getFields();
			Field listening = super.getClass().getField("listening");
			System.out.println(listening);
			if (Boolean.TRUE.equals(listening)) {
				return true;
			}
		} catch (NoSuchFieldException | SecurityException e) {
			LOGGER.error("", e);
		}
		return false;
	}
}
