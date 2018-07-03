package com.g4b.swing.client.pojo;
/**
 * 响应实体
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2017年6月25日 上午11:24:37
 * @version 1.0
 */
public class HttpResponseEntity {
	/** 定义响应状态码 */
	private int statusCode;
	/** 定义响应内容 */
	private String content;
	/** 无参构造器 */
	public HttpResponseEntity(){}
	/** 带参构造器 */
	public HttpResponseEntity(int statusCode, String content) {
		super();
		this.statusCode = statusCode;
		this.content = content;
	}
	/** setter and getter method */
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}