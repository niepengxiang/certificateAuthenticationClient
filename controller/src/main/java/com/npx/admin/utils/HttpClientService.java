package com.npx.admin.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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
import com.npx.admin.pojo.HttpResponseEntity;

/**
 * @ClassName: HttpClientService  
 * @Description: HttpClient客户端服务工具类
 * @author NiePengXiang
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
			//JOptionPane.showMessageDialog(null, "认证同步失败", "提示框", JOptionPane.WARNING_MESSAGE);
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
		String content = null;
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
					/** 设置请求参数 */
					nvpLists.add(new BasicNameValuePair("jsonStrParam", objectMapper.writeValueAsString(params)));
					/** 设置请求参数 */
					httpPost.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			/** 执行请求，得到响应对象 */
			response = httpClient.execute(httpPost);
			/** 获取响应数据 */
			content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			
		}catch(Exception ex){
			//JOptionPane.showMessageDialog(null, "认证同步失败", "提示框", JOptionPane.WARNING_MESSAGE);
			logger.error("发送Post请求失败!",ex);
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
		/** 返回响应实体 */
		return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
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
		String content= null;
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
					nvpLists.add(new BasicNameValuePair("jsonStrParam", objectMapper.writeValueAsString(params)));
					/** 设置请求参数 */
					httpPut.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			/** 执行请求，得到响应对象 */
			response = httpClient.execute(httpPut);
			/** 获取响应数据 */
			content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			/** 返回响应实体 */
			return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
		}catch(Exception ex){
			logger.error("发送Put请求失败!",ex);
			//JOptionPane.showMessageDialog(null, "认证同步失败", "提示框", JOptionPane.WARNING_MESSAGE);
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
		return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
	}
	/**
	 * 利用HttpClient发送DELETE请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应实体
	 */
	public HttpResponseEntity sendDelete(String url, Map<String, Object> params, boolean json){
		/**
         * 没有现成的delete可以带json的，自己实现一个，参考HttpPost的实现
         */
        class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
            public static final String METHOD_NAME = "DELETE";

            @SuppressWarnings("unused")
            public HttpDeleteWithBody() {
            }

            public HttpDeleteWithBody(URI uri) {
                setURI(uri);
            }

            @SuppressWarnings("unused")
			public HttpDeleteWithBody(String uri) {
                setURI(URI.create(uri));
            }

            public String getMethod() {
                return METHOD_NAME;
            }
        }
        
        /** 定义可关闭的响应对象 */
		CloseableHttpResponse response = null;
		String content= null;
		try{
			/** 创建URI */
			URI uri = new URIBuilder(url).build();
			/** 创建Httpdelete请求对象 */
			
	       HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(uri);
			/** 判断是否需要设置请求参数 */
			if (params != null && params.size() > 0){
				if (json){
					StringEntity stringEntity = new StringEntity(objectMapper.writeValueAsString(params), Consts.UTF_8);
		            httpdelete.addHeader("content-type", "application/json");
		            httpdelete.setEntity(stringEntity);
				}else {
					/** 定义List集合封装表单请求参数 */
					List<NameValuePair> nvpLists = new ArrayList<>();
					nvpLists.add(new BasicNameValuePair("jsonStrParam", objectMapper.writeValueAsString(params)));
					/** 设置请求参数 */
					httpdelete.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			/** 执行请求，得到响应对象 */
			response = httpClient.execute(httpdelete);
			/** 获取响应数据 */
			content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			/** 返回响应实体 */
			return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
		}catch(Exception ex){
			logger.error("发送Httpdelete请求失败!",ex);
			//JOptionPane.showMessageDialog(null, "认证同步失败", "提示框", JOptionPane.WARNING_MESSAGE);
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
		return new HttpResponseEntity(response.getStatusLine().getStatusCode(), content);
	}
}