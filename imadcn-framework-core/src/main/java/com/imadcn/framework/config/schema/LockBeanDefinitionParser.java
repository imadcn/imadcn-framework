package com.imadcn.framework.config.schema;

import java.util.Random;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.imadcn.framework.lock.redis.RedisLockManager;

public class LockBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return RedisLockManager.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String id = element.getAttribute("id");
		String group = element.getAttribute("group");
		String redisTemplate = element.getAttribute("redisTemplate");
		String messageContainer = element.getAttribute("messageContainer");
		if (!StringUtils.hasText(group)) {
			throw new IllegalArgumentException("redis lock group name should not be null");
		}
//		if (!StringUtils.hasText(redisTemplate)) {
//			throw new IllegalArgumentException("redis template should not be null");
//		}
//		if (!StringUtils.hasText(messageContainer)) {
//			throw new IllegalArgumentException("redis message listener container should not be null");
//		}
		builder.addPropertyValue("id", StringUtils.hasText(id) ? id : "redisLockManager" + new Random().nextInt(100));
		builder.addPropertyValue("groupName", group);
		if (StringUtils.hasText(redisTemplate)) {
			builder.addPropertyReference("redisTemplate", redisTemplate);
		}
		if (StringUtils.hasText(messageContainer)) {
			builder.addPropertyReference("container", messageContainer);
		}
	}
}
