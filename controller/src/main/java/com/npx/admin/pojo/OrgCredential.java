package com.npx.admin.pojo;

/***
 * @ClassName: OrgCredential
 * @Description: 机构证书同步实体类
 * @author NiePengXiang
 * @date 2018年7月8日
 *
 */
public class OrgCredential {
	
	private String authSrcId;
	private String reqOpt;
	private String sigCertSn;
	private String encCertSn;
	private String sigCer;
	private String encCer;
	private Integer certStatus;
	private String certIssuer;
	private String certUserTypeID;
	private String orgCodeType;
	private String orgCode;
	private String orgName;
	private String legalPerson;
	private String contPerson;
	private String contMobile;
	private String address;
	private String postalCode;
	private String province;
	private String city;
	private String district;
	private String email;
	
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
	public String getEncCertSn() {
		return encCertSn;
	}
	public void setEncCertSn(String encCertSn) {
		this.encCertSn = encCertSn;
	}
	public String getSigCer() {
		return sigCer;
	}
	public void setSigCer(String sigCer) {
		this.sigCer = sigCer;
	}
	public String getEncCer() {
		return encCer;
	}
	public void setEncCer(String encCer) {
		this.encCer = encCer;
	}
	
	public Integer getCertStatus() {
		return certStatus;
	}
	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}
	public String getCertIssuer() {
		return certIssuer;
	}
	public void setCertIssuer(String certIssuer) {
		this.certIssuer = certIssuer;
	}
	public String getCertUserTypeID() {
		return certUserTypeID;
	}
	public void setCertUserTypeID(String certUserTypeID) {
		this.certUserTypeID = certUserTypeID;
	}
	public String getOrgCodeType() {
		return orgCodeType;
	}
	public void setOrgCodeType(String orgCodeType) {
		this.orgCodeType = orgCodeType;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getLegalPerson() {
		return legalPerson;
	}
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	public String getContPerson() {
		return contPerson;
	}
	public void setContPerson(String contPerson) {
		this.contPerson = contPerson;
	}
	public String getContMobile() {
		return contMobile;
	}
	public void setContMobile(String contMobile) {
		this.contMobile = contMobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
