package com.imadcn.framework.web.core;

/**
 * 表明是发送消息还是接收消息
 * @author imadcn
 */
public enum ClassType {
	
	/**
	 * 发送消息
	 */
	SEND("send"),
	/**
	 * 接收消息
	 */
	RECV("recv"),
	;
	
	private String value;
	
	private ClassType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static ClassType getValue(String value) {
		switch (value) {
		case "send": return SEND;
		case "recv": return RECV;
		default: return null;
		}
	}
	
	public static boolean equals(String value, ClassType type) {
		ClassType t = valueOf(value);
		return type != null && t != null && type == t;
	}
}
