package com.imadcn.framework.web.filter;

import com.imadcn.framework.web.core.Message;
import com.imadcn.framework.web.core.Processor;

public interface MessageFilter {
	
	String regex();
	
	Processor doFilter(Message message);

}
