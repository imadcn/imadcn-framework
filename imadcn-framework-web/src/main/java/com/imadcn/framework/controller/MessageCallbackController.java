package com.imadcn.framework.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imadcn.framework.httpclient.HttpClientHelper;

@Controller
@RequestMapping("/message/callback")
public class MessageCallbackController {
	
	protected static final Logger logger = LoggerFactory.getLogger(MessageCallbackController.class);
	
	@RequestMapping("/receive")
	public void receive(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> data) {
		/**
		 * {
			  "class": "recv",
			  "content": "nihao",
			  "group": "Robot",
			  "group_id": 2047537784,
			  "group_uid": 325067042,
			  "id": 23528,
			  "post_type": "receive_message",
			  "receiver": "iRobot",
			  "receiver_id": 3569145307,
			  "receiver_uid": 3569145307,
			  "sender": "iMad",
			  "sender_id": 2928841994,
			  "sender_uid": 97263034,
			  "time": 1490686667,
			  "type": "group_message"
			}
		 */
		logger.info(JSON.toJSONString(data));
		if (data != null) {
			if ("receive_message".equals(data.get("post_type")) && "group_message".equals(data.get("type"))) { // 接受消息
				String content = data.get("content");
				String user = getUser(content);
				String groupUid = data.get("group_uid");
				if (user != null && !user.isEmpty()) {
					String memberUid = getUid(user, groupUid);
					if (memberUid != null && !memberUid.isEmpty()) {
						int minute = new Random().nextInt(10) + 1;
						shutup(groupUid, memberUid, minute);
						sendGroupMessage(groupUid, String.format("%s 被禁言%s分钟", user, minute));
					}
				}
			}
		}
	}
	
	public String getUser(String content) {
		if (content != null) {
			content = content.trim();
			String[] params = content.split(" ");
			if (params != null && params.length > 0) {
				String key = params[0];
				switch (key) {
				case "/tt":
					if (key.length() >=2 ) {
						String user = params[1];
						return user;
					} 
					break;
				case "给":
					break;
				default:
					break;
				}
			}
		}
		return null;
	}

	public void shutup(String groupUid, String memberUid, int minutes) {
		String url = "http://127.0.0.1:5000/openqq/shutup_group_member?group_uid=%s&member_uid=%s&time=%s";
		try {
			HttpClientHelper.sendPostRequest(String.format(url, groupUid, memberUid, minutes * 60));
		} catch (KeyManagementException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error("", e);
		}
	}
	
	public void sendGroupMessage(String groupUid, String content) {
		String url = "http://127.0.0.1:5000/openqq/send_group_message?uid=%s&content=%s";
		try {
			HttpClientHelper.sendPostRequest(String.format(url, groupUid, URLEncoder.encode(content, "UTF-8")));
		} catch (KeyManagementException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error("", e);
		}
	}
	
	public static String searchGroup(String groupUid) {
		String url = "http://127.0.0.1:5000/openqq/search_group?uid=%s";
		try {
			String data = HttpClientHelper.sendPostRequest(String.format(url, groupUid));
			return data;
		} catch (KeyManagementException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error("", e);
		}
		return null;
	}
	
	public static String getUid(String user, String groupUid) {
		String groupData = searchGroup(groupUid);
		if (groupData != null && !groupData.isEmpty()) {
			try {
				JSONArray array = JSONArray.parseArray(groupData);
				if (array != null && array.size() > 0) {
					JSONObject group = array.getJSONObject(0);
					JSONArray members = group.getJSONArray("member");
					if (members != null && members.size() > 0) {
						for (int i = 0; i < members.size(); i++) {
							JSONObject m = members.getJSONObject(i);
							if (user.startsWith("@")) {
								if (user.equals("@" + m.getString("name"))) {
									return m.getString("uid");
								}
							} else {
								if (user.equals(m.getString("uid"))) {
									return m.getString("uid");
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		searchGroup("325067042");
	}
}
