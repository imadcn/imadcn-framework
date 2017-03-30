package com.imadcn.framework.config.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 守望先锋的命名空间处理器.
 * @author yc
 * @since 2017年3月10日
 */
public class OverwatchNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("avatar", new OverwatchBeanDefinitionParser());
	}

}
