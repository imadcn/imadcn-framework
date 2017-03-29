package com.imadcn.framework.web.filter;

import com.imadcn.framework.web.core.Processor;

public interface CommandFilter {
	
	String regex();
	
	Processor doFilter(String command);

}
