package com.imadcn.framework.url.shorten;

import com.imadcn.framework.util.RegexUtil;
import com.imadcn.framework.util.UIDUtil;

/**
 * 短网址
 * @author imadcn
 * @since 2016年7月27日
 */
public class ShortUrl {

	private boolean withHttp = true; // 是否需要带上http://
	
	private String host;
	
	public void setWithHttp(boolean withHttp) {
		this.withHttp = withHttp;
	}

	public void setHost(String host) {
		if (RegexUtil.isEmpty(host)) {
			throw new IllegalArgumentException("host cant be null");
		}
		this.host = host;
	}
	
	/*public void setShortenSize(int shortenSize) {
		if (shortenSize <= 0 || shortenSize > 10) {
			throw new IllegalArgumentException("url size need to between 0(exclusive) and 10(inclusive) (0, 10]");
		}
		this.shortenSize = shortenSize;
	}*/

	public String getShortUrl() {
		StringBuilder url = new StringBuilder();
		String shortKey = UIDUtil.uuid8();
		url.append(getHead()); // start with http?
		url.append(host); // url host
		url.append("/");
		url.append(shortKey);
		return url.toString();
	}
	
	private String getHead() {
		return withHttp ? "http://" : "";
	}
	
	public static void main(String[] args) {
		ShortUrl url = new ShortUrl();
		url.setHost("imadcn.com");
		url.setWithHttp(false);
		for (int i = 0; i < 100; i++) {
			System.out.println(url.getShortUrl());
		}
	}
}
