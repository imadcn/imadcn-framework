package com.imadcn.framework.config.schema;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.imadcn.framework.config.Heroes;
import com.imadcn.framework.config.Overwatch;

public class OverwatchBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return Overwatch.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String id = element.getAttribute("id");  
		String name = element.getAttribute("name");  
        String type = element.getAttribute("type");  
        if (StringUtils.hasText(id)) {  
        	builder.addPropertyValue("id", Integer.valueOf(id));  
        }  
        if (StringUtils.hasText(name)) {  
        	builder.addPropertyValue("name", name);  
        }  
        if (StringUtils.hasText(type)) {  
        	builder.addPropertyValue("type", Enum.valueOf(Heroes.class, type));  
        }
        builder.setLazyInit(false);
	} 
}
