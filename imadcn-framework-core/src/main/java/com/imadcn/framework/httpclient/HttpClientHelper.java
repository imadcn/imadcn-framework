package com.imadcn.framework.httpclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http 客户端请求助手
 * @author yc
 * @since 2017年1月10日
 */
public final class HttpClientHelper {
	/**
	 * 日志对象
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);
	/**
	 * 请求语言
	 */
	private static final String ACCEPT_LANGUAGE = "Accept-Language";
	/**
	 * zh-cn
	 */
	private static final String ZH_CN = "zh-cn";
	/**
	 * utf-8 字符编码
	 */
	private static final String UTF_8 = "UTF-8";

	/**
	 * 获取HttpClient对象
	 * @return {@link HttpClient}
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	protected static CloseableHttpClient getHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		return httpclient;
	}

	/**
	 * 得到post方式的Http请求对象
	 * 
	 * @param uri {@link String} 请求地址
	 * @return {@link HttpPost}
	 */
	protected static HttpPost getHttpPost(String uri) {
		// 创建post请求
		return new HttpPost(uri);
	}

	/**
	 * 得到get方式的Http请求对象
	 * 
	 * @param uri
	 *            {@link String} 请求地址
	 * @return {@link HttpGet}
	 */
	protected static HttpGet getHttpGet(String uri) {
		// 创建get请求
		return new HttpGet(uri);
	}

	/**
	 * 设置请求报头
	 * 
	 * @param httpGet {@link HttpGet} get请求Http
	 * @return {@link HttpGet}
	 */
	protected static HttpGet setHeader(HttpGet httpGet, Map<String, String> headerMap) {
		if (headerMap != null) {
			for (Map.Entry<String, String> map : headerMap.entrySet()) {
				httpGet.setHeader(map.getKey(), map.getValue());
			}
		}
		// 设置接收语言
		httpGet.setHeader(ACCEPT_LANGUAGE, ZH_CN);
		return httpGet;
	}

	/**
	 * 设置请求报头
	 * 
	 * @param httpPost
	 *            {@link HttpPost} post请求Http
	 * @return {@link HttpPost}
	 */
	protected static HttpPost setHeader(HttpPost httpPost, Map<String, String> headerMap) {
		if (headerMap != null) {
			for (Map.Entry<String, String> map : headerMap.entrySet()) {
				httpPost.setHeader(map.getKey(), map.getValue());
			}
		}
		// 设置接收语言
		httpPost.setHeader(ACCEPT_LANGUAGE, ZH_CN);
		return httpPost;
	}

	/**
	 * 
	 * 发送POST请求
	 * 
	 * @param uri {@link String} 请求地址
	 * @return {@link String}
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostRequest(String uri) throws KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {
		return sendPostRequest(uri, null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param uri
	 *            {@link String} 请求地址
	 * @param paramMap
	 *            {@link Map} 请求参数
	 * @return {@link String}
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostRequest(String uri, Map<String, String> paramMap) throws KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {
		return sendPostRequest(uri, paramMap, null, UTF_8);
	}

	/**
	 * 
	 * @param uri {@link String} 请求地址
	 * @param paramMap {@link Map} 请求参数
	 * @param headerMap {@link Map} 请求头
	 * @return {@link String}
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostRequest(String uri, Map<String, String> paramMap, Map<String, String> headerMap) throws KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {
		return sendPostRequest(uri, paramMap, headerMap, UTF_8);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param uri {@link String} 请求地址
	 * @param paramMap {@link Map} 请求参数
	 * @param headerMap {@link Map} 请求头
	 * @param code {@link String} 请求参数编码
	 * @return {@link String}
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostRequest(String uri, Map<String, String> paramMap, Map<String, String> headerMap, String code) throws KeyManagementException, NoSuchAlgorithmException,
			UnsupportedEncodingException {
		LOGGER.debug("execute sendPostRequest begin");
		long startTime = System.currentTimeMillis();
		// 创建客户端
		CloseableHttpClient httpclient = getHttpClient();
		LOGGER.info("sendRequest url = " + uri);
		HttpPost post = getHttpPost(uri);
		setHeader(post, headerMap);
		String responseBody = null;
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		if (paramMap != null) {
			// 设置post参数
			for (Map.Entry<String, String> m : paramMap.entrySet()) {
				paramList.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				LOGGER.info("Param KEY = [" + m.getKey() + "] & VALUE = [" + m.getValue() + "]");
			}
			if (paramList != null && paramList.size() > 0) {
				UrlEncodedFormEntity uef = new UrlEncodedFormEntity(paramList, code);
				post.setEntity(uef);
			}
		}
		try {
			HttpResponse response = httpclient.execute(post);
			responseBody = readInputStream(response.getEntity().getContent());
			LOGGER.info("\n" + responseBody + "\n");
			LOGGER.info("sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			LOGGER.error("execute sendPostRequest exception ", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("execute close httpclient exception ", e);
			}
		}
		LOGGER.debug("execute sendPostRequest end");
		return responseBody;
	}

	/**
	 * 
	 * 发送POST请求
	 * @param uri {@link String} 请求地址
	 * @param entity {@link UrlEncodedFormEntity} 请求实体
	 * @param headerMap  {@link Map} 请求头
	 * @param code {@link String} 请求参数编码
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostRequest(String uri, UrlEncodedFormEntity entity, Map<String, String> headerMap, String code) throws KeyManagementException, NoSuchAlgorithmException,
			UnsupportedEncodingException {
		LOGGER.debug("execute sendPostRequest begin");
		long startTime = System.currentTimeMillis();
		// 创建客户端
		CloseableHttpClient httpclient = getHttpClient();
		LOGGER.info("sendRequest url = " + uri);
		HttpPost post = getHttpPost(uri);
		String responseBody = null;
		try {
			post.setEntity(entity);
			setHeader(post, headerMap);
			HttpResponse response = httpclient.execute(post);
			responseBody = readInputStream(response.getEntity().getContent());
			LOGGER.info("\n" + responseBody + "\n");
			LOGGER.info("sendPostRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			LOGGER.error("execute sendPostRequest exception ", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("execute close httpclient exception ", e);
			}
		}
		LOGGER.debug("execute sendPostRequest end");
		return responseBody;
	}

	/**
	 * 发送GET请求
	 * 
	 * @param uri
	 *            {@link String} 请求地址
	 * @return {@link String}
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String sendGetRequest(String uri) throws KeyManagementException, NoSuchAlgorithmException {
		return sendGetRequest(uri, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param uri {@link String} 请求地址
	 * @param headerMap {@link String} 请求头
	 * @return {@link String}
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String sendGetRequest(String uri, Map<String, String> headerMap) throws KeyManagementException, NoSuchAlgorithmException {
		LOGGER.debug("execute sendGetRequest begin");
		long startTime = System.currentTimeMillis();
		// 创建客户端
		CloseableHttpClient httpclient = getHttpClient();
		LOGGER.info("sendGetRequest url = " + uri);
		HttpGet get = getHttpGet(uri);
		setHeader(get, headerMap);
		String responseBody = null;
		try {
			HttpResponse response = httpclient.execute(get);
			responseBody = readInputStream(response.getEntity().getContent());
			LOGGER.info("\n" + responseBody + "\n");
			LOGGER.info("sendGetRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			LOGGER.error("execute sendGetRequest exception ", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("execute close httpclient exception ", e);
			}
		}
		LOGGER.debug("execute sendGetRequest end");
		return responseBody;
	}

	/**
	 * 发送GET请求
	 * 
	 * @param uri
	 *            {@link String} 请求地址
	 * @return {@link Byte} byte[]
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] sendGetRequestStream(String uri) throws KeyManagementException, NoSuchAlgorithmException {
		LOGGER.debug("execute sendGetRequestStream begin");
		long startTime = System.currentTimeMillis();
		// 创建客户端
		CloseableHttpClient httpclient = getHttpClient();
		LOGGER.info("sendGetRequestStream url = " + uri);
		HttpGet get = getHttpGet(uri);
		setHeader(get, null);
		byte imgdata[] = null;
		try {
			HttpResponse response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
				int ch;
				while ((ch = instream.read()) != -1) {
					bytestream.write(ch);
				}
				imgdata = bytestream.toByteArray();
				bytestream.close();
				instream.close();
			}
			LOGGER.info("sendGetRequestStream method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			LOGGER.error("execute sendGetRequestStream exception ", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("execute close httpclient exception ", e);
			}
		}
		LOGGER.debug("execute sendGetRequestStream end");
		return imgdata;
	}
	
	/**
	 * 发送xml字符串请求
	 * 
	 * @param uri 请求地址
	 * @param xmlStr xml字符串
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static String sendPostXmlRequest(String uri,String xmlStr) throws KeyManagementException, NoSuchAlgorithmException{
		LOGGER.debug("execute sendGetRequest begin");
		long startTime = System.currentTimeMillis();
		// 创建客户端
		CloseableHttpClient httpclient = getHttpClient();
		LOGGER.info("sendPostRequest url = " + uri);
		HttpPost post = getHttpPost(uri);
		String retStr = "";
		try {
			StringEntity myEntity = new StringEntity(xmlStr, "UTF-8");
			post.addHeader("Content-Type", "text/xml");
			post.setEntity(myEntity);
			HttpResponse response = httpclient.execute(post);
			HttpEntity resEntity = response.getEntity();
			InputStreamReader reader = new InputStreamReader(resEntity.getContent(), "UTF-8");
			char[] buff = new char[1024];
			int length = 0;
			while ((length = reader.read(buff)) != -1) {
				retStr = new String(buff, 0, length);
			}
			LOGGER.info("\n" + retStr + "\n");
			LOGGER.info("sendGetRequest method execute time is [" + (System.currentTimeMillis() - startTime) + "] ms");
		} catch (Exception e) {
			LOGGER.error("execute sendGetRequest exception ", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("execute close httpclient exception ", e);
			}
		}
		LOGGER.debug("execute sendGetRequest end");
		return retStr;
	}

	/**
	 * 处理返回文件流
	 * 
	 * @param inputStream {@link InputStream} 输入流
	 * @return {@link String}
	 * @throws IOException
	 */
	private static String readInputStream(InputStream inputStream) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null)
			buffer.append(line + "\n");
		inputStream.close();
		return buffer.toString();
	}

	/**
	 * 
	 * 格式化参数
	 * 
	 * @param paramMap  {@link Map} 输入参数
	 * @return {@link String}
	 */
	@SuppressWarnings("rawtypes")
	public static String formatParamMap(Map<String, String> paramMap) {
		String retStr = "";
		for (Iterator iter = paramMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			retStr += "&" + entry.getKey() + "=" + entry.getValue();
		}
		return retStr;
	}
	
	/**
	 * 
	 * 格式化参数
	 * @param {@link Map} paramMap 输入参数
	 * @param {@link String} code url编码格式
	 * @return {@link String}
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	public static String formatParamMap(Map<String, String> paramMap, String code) throws UnsupportedEncodingException {
		String retStr = "";
		for (Iterator iter = paramMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			retStr += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), code) + "&";
		}
		return retStr.substring(0, retStr.length() - 1);
	}

	/**
	 * 
	 * 获取参数对象
	 * 
	 * @param paramMap
	 * @param code
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static UrlEncodedFormEntity getHttpParamLength(Map<String, String> paramMap, String code) throws UnsupportedEncodingException {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity uef = null;
		if (paramMap != null) {
			// 设置post参数
			for (Map.Entry<String, String> m : paramMap.entrySet()) {
				paramList.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				LOGGER.info("Param KEY = [" + m.getKey() + "] & VALUE = [" + m.getValue() + "]");
			}
			if (paramList != null && paramList.size() > 0) {
				uef = new UrlEncodedFormEntity(paramList, code);
			}
		}
		return uef;
	}
}
