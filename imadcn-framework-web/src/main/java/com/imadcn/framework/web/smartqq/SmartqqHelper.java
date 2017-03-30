package com.imadcn.framework.web.smartqq;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imadcn.framework.httpclient.HttpClientHelper;

public class SmartqqHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SmartqqHelper.class);

	private static String host;
	private static int port;
	
	public static void shutupGroupMember(String groupUid, String memberUid, int minutes) {
		String uri = "http://%s:%d/openqq/shutup_group_member?group_uid=%s&member_uid=%s&time=%s";
		try {
			HttpClientHelper.sendPostRequest(String.format(uri, host, port, groupUid, memberUid, minutes * 60));
		} catch (KeyManagementException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error("", e);
		}
	}
	
	public static void sendGroupMessage(String groupUid, String content) {
		String uri = "http://%s:%d/openqq/send_group_message?uid=%s&content=%s";
		try {
			HttpClientHelper.sendPostRequest(String.format(uri, host, port, groupUid, URLEncoder.encode(content, "UTF-8")));
		} catch (KeyManagementException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error("", e);
		}
	}
	
	public static String searchGroup(String groupUid) {
		String uri = "http://127.0.0.1:5000/openqq/search_group?uid=%s";
		try {
			String data = HttpClientHelper.sendPostRequest(String.format(uri, host, port, groupUid));
			return data;
		} catch (KeyManagementException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			logger.error("", e);
		}
		return null;
	}

	public static void setHost(String host) {
		SmartqqHelper.host = host;
	}

	public static void setPort(int port) {
		SmartqqHelper.port = port;
	}
	

}
