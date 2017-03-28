package com.imadcn.demo.smartqq;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.imadcn.framework.httpclient.HttpClientHelper;

public class SmartqqTest {
	
	public static void main(String[] args) {
		String groupUid = "325067042";
		String memberUid = "2323745783";
		int time = 24 * 24 * 60 * 60;
		String url = "http://127.0.0.1:5000/openqq/shutup_group_member?group_uid=%s&member_uid=%s&time=%s";
		
		try {
			HttpClientHelper.sendPostRequest(String.format(url, groupUid, memberUid, time));
		} catch (KeyManagementException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
