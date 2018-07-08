package com.npx.admin.pojo;

import java.util.List;

/**
 * @ClassName: CaPerson  
 * @Description: CA用户同步实体类  
 * @author niepengxiang
 * @date 2018年7月8日  
 *
 */
public class CaPerson {
	private String authSrcId;
	private String reqOpt;
	private String sigCertSn;
	private Integer accessCtrlOpt;
	private List<String> appSysIds;
	public String getAuthSrcId() {
		return authSrcId;
	}
	public void setAuthSrcId(String authSrcId) {
		this.authSrcId = authSrcId;
	}
	public String getReqOpt() {
		return reqOpt;
	}
	public void setReqOpt(String reqOpt) {
		this.reqOpt = reqOpt;
	}
	public String getSigCertSn() {
		return sigCertSn;
	}
	public void setSigCertSn(String sigCertSn) {
		this.sigCertSn = sigCertSn;
	}
	public Integer getAccessCtrlOpt() {
		return accessCtrlOpt;
	}
	public void setAccessCtrlOpt(Integer accessCtrlOpt) {
		this.accessCtrlOpt = accessCtrlOpt;
	}
	public List<String> getAppSysIds() {
		return appSysIds;
	}
	public void setAppSysIds(List<String> appSysIds) {
		this.appSysIds = appSysIds;
	}
	
}
