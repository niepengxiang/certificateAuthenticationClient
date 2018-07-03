package com.g4b.swing.client.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g4b.swing.client.pojo.HttpResponseEntity;

/**
 * @ClassName: HttpClientService  
 * @Description: HttpClient客户端服务工具类
 * @author niepengxiang
 * @date 2018年7月3日  
 */
public class HttpClientService {
	
	private static Logger logger = Logger.getLogger(HttpClientService.class);
	
	/** 定义可关闭的HttpClient客户端对象 */
	private CloseableHttpClient httpClient = HttpClients.createDefault(); 
	/** 定义ObjectMapper操作json */
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * 利用HttpClient发送GET请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应实体
	 */
	public HttpResponseEntity sendGet(String url, Map<String, Object> params){
		logger.info("发送Get请求：" + url);
		/** 定义可关闭的响应对象 */
		CloseableHttpResponse response = null;
		try{
			/** 创建URI */
			URIBuilder builder = new URIBuilder(url);
			/** 判断是否需要设置请求参数 */
			if (params != null && params.size() > 0){
				for (Map.Entry<String, Object> entry : params.entrySet()){
					/** 设置请求参数 */
					builder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
				}
			}
			/** 创建HttpGet请求对象 */
			HttpGet httpGet = new HttpGet(builder.build());
			/** 执行请求，得到响应对象 */
			response = httpClient.execute(httpGet);
			/** 获取响应数据 */
			String content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			/** 返回响应实体 */
			return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
		}catch(Exception ex){
			logger.error("发送get请求失败!",ex);
			throw new RuntimeException(ex);
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
	}
	/**
	 * 利用HttpClient发送POST请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应实体
	 */
	public HttpResponseEntity sendPost(String url, Map<String, Object> params, boolean json){
		logger.info("发送Post请求：" + url);
		/** 定义可关闭的响应对象 */
		CloseableHttpResponse response = null;
		try{
			/** 创建URI */
			URI uri = new URIBuilder(url).build();
			/** 创建HttpPost请求对象 */
			HttpPost httpPost = new HttpPost(uri);
			/** 判断是否需要设置请求参数 */
			if (params != null && params.size() > 0){
				if (json){ // application/json
					httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(params),
							ContentType.APPLICATION_JSON));
				}else{ // application/x-www-form-urlencoded
					/** 定义List集合封装表单请求参数 */
					List<NameValuePair> nvpLists = new ArrayList<>();
					for (Map.Entry<String, Object> entry : params.entrySet()){
						/** 设置请求参数 */
						nvpLists.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
					}
					/** 设置请求参数 */
					httpPost.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			/** 执行请求，得到响应对象 */
			response = httpClient.execute(httpPost);
			/** 获取响应数据 */
			String content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			/** 返回响应实体 */
			return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
		}catch(Exception ex){
			logger.error("发送Post请求失败!",ex);
			throw new RuntimeException(ex);
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
	}
	/**
	 * 利用HttpClient发送PUT请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应实体
	 */
	public HttpResponseEntity sendPut(String url, Map<String, Object> params, boolean json){
		logger.info("发送Put请求：" + url);
		/** 定义可关闭的响应对象 */
		CloseableHttpResponse response = null;
		try{
			/** 创建URI */
			URI uri = new URIBuilder(url).build();
			/** 创建HttpPut请求对象 */
			HttpPut httpPut = new HttpPut(uri);
			/** 判断是否需要设置请求参数 */
			if (params != null && params.size() > 0){
				
				if (json){
					httpPut.setEntity(new StringEntity(objectMapper.writeValueAsString(params),
							ContentType.APPLICATION_JSON));
				}else{
					/** 定义List集合封装表单请求参数 */
					List<NameValuePair> nvpLists = new ArrayList<>();
					for (Map.Entry<String, Object> entry : params.entrySet()){
						/** 设置请求参数 */
						nvpLists.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
					}
					/** 设置请求参数 */
					httpPut.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			/** 执行请求，得到响应对象 */
			response = httpClient.execute(httpPut);
			/** 获取响应数据 */
			String content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			/** 返回响应实体 */
			return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
		}catch(Exception ex){
			logger.error("发送Put请求失败!",ex);
			throw new RuntimeException(ex);
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
	}
	/**
	 * 利用HttpClient发送DELETE请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应实体
	 */
	public HttpResponseEntity sendDelete(String url, Map<String, Object> params){
		logger.info("发送Delete请求：" + url);
		/** 判断请求参数Map集合 */
		if (params == null){
			params = new HashMap<>();
		}
		params.put("_method", "delete");
		return sendPost(url, params, true);
	}
}