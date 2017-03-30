package com.imadcn.framework.web.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.imadcn.framework.web.core.Message;
import com.imadcn.framework.web.core.Processor;

@Component
public class ReceiveMessageExecutor {
	
	@Autowired(required = false)
	private List<MessageFilter> filters;
	
	public void doFilter(Message message) {
		if (filters != null && !filters.isEmpty()) {
			for (MessageFilter mf : filters) {
				Processor p = mf.doFilter(message);
				if (p != null) {
					p.onCall(message);
					break;
				}
			}
		}
	}

}
