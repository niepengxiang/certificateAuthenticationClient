package com.npx.admin.pojo;
/**
 * @ClassName: HttpResponseEntity  
 * @Description: TODO HTTP返回实体类
 * @author NiePengXiang
 * @date 2018年7月4日  
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