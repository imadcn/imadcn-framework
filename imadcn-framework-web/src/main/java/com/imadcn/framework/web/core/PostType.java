package com.imadcn.framework.web.core;

public enum PostType {

	/**
	 * 接收消息
	 */
	RECEIVE_MESSAGE("receive_message"),
	/**
	 * 发送消息
	 */
	SEND_MESSAGE("send_message"),
	/**
	 * 其他事件
	 */
	EVENT("event"),
	;
	
	private String value;
	
	private PostType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static PostType getValue(String value) {
		switch (value) {
		case "receive_message": return RECEIVE_MESSAGE;
		case "send_message": return SEND_MESSAGE;
		case "event": return EVENT;
		default: return null;
		}
	}
	
	public static boolean equals(String value, PostType type) {
		PostType t = getValue(value);
		return type != null && t != null && type == t;
	}
}
