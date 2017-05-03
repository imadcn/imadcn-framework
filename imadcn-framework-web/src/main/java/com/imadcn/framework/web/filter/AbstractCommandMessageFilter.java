package com.imadcn.framework.web.filter;

import com.imadcn.framework.web.core.Message;

public /*abstract*/ class AbstractCommandMessageFilter/* implements MessageFilter */{
	
	protected static String getCommand(Message message) {
		String content = message.getContent();
		if (content != null) {
			content = content.trim();
			String[] tmp = content.split(" ", 3);
			if (tmp != null && tmp.length >= 2) {
				return tmp[1].trim();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String content = "  /tt  smoke 10";
		if (content != null) {
			content = content.trim();
			String regex = "\\s*/tt\\s+\\w+\\s+.+";
			System.out.println(content.replaceAll(regex, "$1"));
		}
	}
	
}
