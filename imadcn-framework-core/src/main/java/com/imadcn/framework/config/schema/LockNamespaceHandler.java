package com.imadcn.framework.config.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 分布式锁的命名空间处理器.
 * @author yc
 * @since 2017年3月9日
 */
public class LockNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("config", new LockBeanDefinitionParser());
	}

}
