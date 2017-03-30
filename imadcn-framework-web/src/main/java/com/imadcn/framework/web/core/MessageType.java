package com.imadcn.framework.web.core;

/**
 * 消息类型细分
 * @author imadcn
 */
public enum MessageType {
	
	/**
	 * 好友消息
	 */
	FRIEND_MESSAGE("friend_message"),
	/**
	 * 群消息
	 */
	GROUP_MESSAGE("group_message"),
	/**
	 * 讨论组消息
	 */
	DISCUSS_MESSAGE("discuss_message"),
	/**
	 * 临时消息
	 */
	SESS_MESSAGE("sess_message"),
	;
	
	private String value;
	
	private MessageType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
	public static MessageType getValue(String value) {
		switch (value) {
		case "friend_message": return FRIEND_MESSAGE;
		case "group_message": return GROUP_MESSAGE;
		case "discuss_message": return DISCUSS_MESSAGE;
		case "sess_message": return SESS_MESSAGE;
		default: return null;
		}
	}
	
	public static boolean equals(String value, MessageType type) {
		MessageType t = getValue(value);
		return type != null && t != null && type == t;
	}
}
