package com.imadcn.demo.elasticsearch;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSON;

public class EsDemo {
	
	private TransportClient client;
	
	@SuppressWarnings("resource")
	public EsDemo() {
		client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
	}
	
	public String getJsonData() {
		Map<String, Object> json = new HashMap<>();
		json.put("user", "kimchy");
		json.put("postDate", new Date());
		json.put("message", "我们建立一个网站或应用程序，并要添加搜索功能，令我们受打击的是：搜索工作是很难的。我们希望我们的搜索解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP的索引数据，我们希望我们的搜索服务器始终可用，我们希望能够一台开始并扩展到数百，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。Elasticsearch旨在解决所有这些问题和更多的问题。");
		return JSON.toJSONString(json);
	}
	
	public void createIndex() {
		IndexResponse idxRsp = client.prepareIndex("weibo", "isay", "kimchy").setSource(getJsonData()).get();
		print(idxRsp);
	}
	
	protected void print(Object object) {
		System.err.println(JSON.toJSONString(object));
	}
	
	public static void main(String[] args) throws Exception {
		EsDemo demo = new EsDemo();
		demo.createIndex();
	}
	
	

}
