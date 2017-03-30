package com.imadcn.framework.web.core;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 发送接收消息（PostType为RECEIVE_MESSAGE或SEND_MESSAGE时）的关键属性信息
 * @author imadcn
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = -151197972844991485L;

	@JSONField(name="class")
	private String clazz;
	
	private String content;
	
	private String group;
	
	@JSONField(name="group_id")
	private String groupId;
	
	@JSONField(name="group_uid")
	private String groupUid;
	
	private String id;
	
	@JSONField(name="post_type")
	private String postType;

	private String receiver;
	
	@JSONField(name="receiver_id")
	private String receiverId;

	@JSONField(name="receiver_uid")
	private String receiverUid;
	
	private String sender;
	
	@JSONField(name="sender_id")
	private String senderId;
	
	@JSONField(name="sender_uid")
	private String senderUid;
	
	private Date time;
	
	private String type;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupUid() {
		return groupUid;
	}

	public void setGroupUid(String groupUid) {
		this.groupUid = groupUid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverUid() {
		return receiverUid;
	}

	public void setReceiverUid(String receiverUid) {
		this.receiverUid = receiverUid;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderUid() {
		return senderUid;
	}

	public void setSenderUid(String senderUid) {
		this.senderUid = senderUid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
