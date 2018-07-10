package com.npx.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.npx.admin.intercepter.DataModule;
import com.npx.admin.pojo.CaCredential;
import com.npx.admin.pojo.HttpResponseEntity;
import com.npx.admin.pojo.OrgCredential;
import com.npx.admin.pojo.OrgPersonCredential;
import com.npx.admin.pojo.Result;
import com.npx.admin.utils.HttpClientService;
import com.npx.admin.utils.PkiUtils;
import com.npx.admin.utils.ReadExcelTools;

@Controller
public class FileUploadController implements DataModule {

	Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	@Qualifier("httpClientService")
	private HttpClientService httpClinetService;
	
	
	@Value("${orginfoIp}")
	private String orginfoIp;
	
	@Value("${personinfoIp}")
	private String personinfoIp;
	
	@Value("${appaccessctrlIp}")
	private String appaccessctrlIp;
	
	@Value("${certchainIp}")
	private String certchainIp;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Result importExcel(@RequestParam(value = "excelFile", required = false) MultipartFile[] file,
			HttpServletRequest request) {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			/** 文件上传批量上传非空判断 */
			if (file != null && file.length > 0) {
				/** 迭代上传的文件数组 */
				for (MultipartFile excelFile : file) {
					Map<String, Object> dataMap = new HashMap<>();
					List<OrgCredential> credentials = new ArrayList<>();
					List<OrgPersonCredential> orgPersonCredentials = new ArrayList<>();
					List<Map<String, Object>> caPersons = new ArrayList<>();
					List<CaCredential> caCredentials = new ArrayList<>();

					/** 非空判断 */
					if (excelFile != null) {
						/** 获取上传文件名称 */
						String fileName = excelFile.getOriginalFilename();

						/**
						 * 传入文件的输入流和文件名称 文件名判断Excel后缀名判断不同的类型 获取文件的第一行的标题
						 */
						String firstCellValue = (String) ReadExcelTools.getFirstCellValue(excelFile.getInputStream(),
								fileName);
						List<StringBuffer> buffers = new ArrayList<>();
						/** 标题判断 */
						if (firstCellValue.equals(ORGCREDENTIAL)) {

							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
							for (int i = 0; i < readExcel.size(); i++) {

								OrgCredential orgCredential = new OrgCredential();
								List<Object> model = readExcel.get(i);
								orgCredential.setAuthSrcId((String) model.get(0));
								
								if(((String) model.get(1)).trim().equals("新增")) {
									orgCredential.setReqOpt("I");
								}else if(((String) model.get(1)).trim().equals("更新")) {
									orgCredential.setReqOpt("U");
								}else if(((String) model.get(1)).trim().equals("删除")) {
									orgCredential.setReqOpt("D");
								}
								orgCredential.setSigCertSn((String) model.get(2));
								orgCredential.setEncCertSn((String) model.get(3));
								orgCredential.setSigCer((String) model.get(4));
								
								orgCredential.setEncCer(((String) model.get(5)));
								
								if(((String) model.get(6)).trim().equals("有效")) {
									orgCredential.setCertStatus(1);
								}else if(((String) model.get(6)).trim().equals("无效")) {
									orgCredential.setCertStatus(0);
								}
								
								orgCredential.setCertIssuer((String) model.get(7));
								
								if(((String) model.get(8)).trim().equals("机构用户证书")) {
									orgCredential.setCertUserTypeID("ORG");
								}else if(((String) model.get(8)).trim().equals("业务员用户证书")) {
									orgCredential.setCertUserTypeID("ORG_PERSON");
								}else if(((String) model.get(8)).trim().equals("个人用户证书")) {
									orgCredential.setCertUserTypeID("PERSON");
								}
								if(((String) model.get(9)).trim().equals("其他")) {
									orgCredential.setOrgCodeType("0");
								}else if(((String) model.get(9)).trim().equals("统一社会信任代码")) {
									orgCredential.setOrgCodeType("1");
								}else if(((String) model.get(9)).trim().equals("企业注册号")) {
									orgCredential.setOrgCodeType("2");
								}
								
								orgCredential.setOrgCode((String) model.get(10));
								orgCredential.setOrgName((String) model.get(11));
								orgCredential.setLegalPerson((String) model.get(12));
								orgCredential.setContPerson((String) model.get(13));
								orgCredential.setContMobile((String) model.get(14));
								orgCredential.setAddress((String) model.get(15));
								orgCredential.setPostalCode((String) model.get(16));
								orgCredential.setProvince((String) model.get(17));
								orgCredential.setCity((String) model.get(18));
								orgCredential.setDistrict((String) model.get(19));
								orgCredential.setEmail((String) model.get(20));
								credentials.add(orgCredential);
							}

							dataMap.put("transContent", credentials);
							String sign = PkiUtils.sign(objectMapper.writeValueAsString(credentials));
							dataMap.put("transSignedValue", sign);
							HttpResponseEntity responseEntity = httpClinetService.sendPost(orginfoIp, dataMap, false);
							
							/** 判断返回状态码 */
							if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
								String content = responseEntity.getContent();
								/** 解析返回JSON数据 */
								Map<String, Object> readValue = objectMapper.readValue(content, Map.class);

								StringBuffer dataBuffer = new StringBuffer();
								/** PK签名值判断 */
								//if (PkiUtils.checkSign(content, (String) readValue.get(TRANSSIGNEDVALUE))) {
									Map<String, Object> transContent = (Map<String, Object>) readValue.get(TRANSCONTENT);
									
									/** 返回数据内容 */
									if (transContent != null && transContent.size() > 0) {
										StringBuffer stringBuffer = new StringBuffer();
										stringBuffer.append(ZH_AUTHSRCID).append(transContent.get(AUTHSRCID))
												.append("</br>").append(ZH_RESULTSTATE);
										if ((Integer)transContent.get(RESULTSTATE) == 0) {
											stringBuffer.append(ZH_RESULTSTATE1).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 1) {
											stringBuffer.append(ZH_RESULTSTATE2).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 2) {
											stringBuffer.append(ZH_RESULTSTATE3).append("</br>");
										}

										stringBuffer.append(ZH_TOTALNUM).append(transContent.get(TOTALNUM))
												.append("</br>").append(ZH_FAILNUM).append(transContent.get(FAILNUM))
												.append("</br>");

										List<Map<String, Object>> failArrs = (List<Map<String, Object>>) transContent.get("failArr");
										if (failArrs != null && failArrs.size() > 0) {
											StringBuffer failArrsBuffer = new StringBuffer();
											for (int j = 0; j < failArrs.size(); j++) {
												if (j != 0) {
													failArrsBuffer.append("</br>");
												}
												
											  failArrsBuffer.append(ZH_REQOPT);
											  Object reqOpt = failArrs.get(j).get(REQOPT);
											  if(reqOpt.equals("I")) {
												  failArrsBuffer.append(REQOPTADD).append("</br>");
											  }else if(reqOpt.equals("U")) {
												  failArrsBuffer.append(REQOPTUPDATE).append("</br>");
											  }else if(reqOpt.equals("D")) {
												  failArrsBuffer.append(REQOPTDELETE).append("</br>");
											  }
											  
											  
											  failArrsBuffer.append(ZH_SIGCERTSN).append(failArrs.get(j).get(SIGCERTSN)).append("</br>")
															.append(ZH_ERRORCODE);
												Object errorCode = failArrs.get(j).get(ERRORCODE);
												if (errorCode.equals(ERRORCODENUM0)) {
													failArrsBuffer.append(ZH_ERRORCODENUM0).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM1)) {
													failArrsBuffer.append(ZH_ERRORCODENUM1).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM2)) {
													failArrsBuffer.append(ZH_ERRORCODENUM2).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM3)) {
													failArrsBuffer.append(ZH_ERRORCODENUM3).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM4)) {
													failArrsBuffer.append(ZH_ERRORCODENUM4).append("</br>");
												}
												failArrsBuffer.append(ZH_ERRORMSG);
												Object errorMsg = failArrs.get(j).get(ERRORMSG);
												if (errorMsg != null) {
													failArrsBuffer.append(failArrs.get(j).get(ERRORMSG));
												}
											}
											stringBuffer.append("</br>").append(failArrsBuffer.toString());
										}

										dataBuffer.append(stringBuffer.toString());
									}
								//} 
								buffers.add(dataBuffer);
							}

							/** 标题判断 */
						} else if (firstCellValue.equals(ORGPERSONCREDENTIAL)) {
							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
							for (int i = 0; i < readExcel.size(); i++) {
								OrgPersonCredential orgPersonCredential = new OrgPersonCredential();
								List<Object> model = readExcel.get(i);
								orgPersonCredential.setAuthSrcId((String) model.get(0));
								
								if(((String) model.get(1)).trim().equals("新增")) {
									orgPersonCredential.setReqOpt("I");
								}else if(((String) model.get(1)).trim().equals("更新")) {
									orgPersonCredential.setReqOpt("U");
								}else if(((String) model.get(1)).trim().equals("删除")) {
									orgPersonCredential.setReqOpt("D");
								}
								orgPersonCredential.setSigCertSn((String) model.get(2));
								orgPersonCredential.setEncCertSn((String) model.get(3));
								orgPersonCredential.setSigCer((String) model.get(4));
								orgPersonCredential.setEncCer((String) model.get(5));
								
								if(((String) model.get(6)).trim().equals("有效")) {
									orgPersonCredential.setCertStatus(1);
								}else if(((String) model.get(6)).trim().equals("无效")) {
									orgPersonCredential.setCertStatus(0);
								}
								orgPersonCredential.setCertIssuer((String) model.get(7));
								orgPersonCredential.setUserName((String) model.get(8));
								
								if(((String) model.get(9)).trim().equals("机构用户证书")) {
									orgPersonCredential.setCertUserTypeID("ORG");
								}else if(((String) model.get(9)).trim().equals("业务员用户证书")) {
									orgPersonCredential.setCertUserTypeID("ORG_PERSON");
								}else if(((String) model.get(9)).trim().equals("个人用户证书")) {
									orgPersonCredential.setCertUserTypeID("PERSON");
								}
								
								if(((String) model.get(10)).trim().equals("男")) {
									orgPersonCredential.setSex(1);
								}else if(((String) model.get(10)).trim().equals("女")) {
									orgPersonCredential.setSex(2);
								}
								
								if(((String) model.get(11)).trim().equals("身份证")) {
									orgPersonCredential.setCredentialType("0");
								}else if(((String) model.get(11)).trim().equals("护照")) {
									orgPersonCredential.setCredentialType("1");
								}else if(((String) model.get(11)).trim().equals("军官证")) {
									orgPersonCredential.setCredentialType("2");
								}else if(((String) model.get(11)).trim().equals("士兵证")) {
									orgPersonCredential.setCredentialType("3");
								}else if(((String) model.get(11)).trim().equals("港澳台居民往来通行证")) {
									orgPersonCredential.setCredentialType("4");
								}else if(((String) model.get(11)).trim().equals("临时身份证")) {
									orgPersonCredential.setCredentialType("5");
								}else if(((String) model.get(11)).trim().equals("户口本")) {
									orgPersonCredential.setCredentialType("6");
								}else if(((String) model.get(11)).trim().equals("其他")) {
									orgPersonCredential.setCredentialType("7");
								}else if(((String) model.get(11)).trim().equals("警官证")) {
									orgPersonCredential.setCredentialType("8");
								}else if(((String) model.get(11)).trim().equals("外国人永久居留证")) {
									orgPersonCredential.setCredentialType("9");
								}else if(((String) model.get(11)).trim().equals("营业执照")) {
									orgPersonCredential.setCredentialType("10");
								}
								
								orgPersonCredential.setCredentialNum((String) model.get(12));
								orgPersonCredential.setJobTitle((String) model.get(13));
								orgPersonCredential.setMobile((String) model.get(14));
								orgPersonCredential.setAddress((String) model.get(15));
								orgPersonCredential.setPostalCode((String) model.get(16));
								orgPersonCredential.setProvince((String) model.get(17));
								orgPersonCredential.setCity((String) model.get(18));
								orgPersonCredential.setDistrict((String) model.get(19));
								orgPersonCredential.setEmail((String) model.get(20));
								orgPersonCredentials.add(orgPersonCredential);
							}

							dataMap.put("transContent", credentials);
							String sign = PkiUtils.sign(objectMapper.writeValueAsString(caCredentials));
							dataMap.put("transSignedValue", sign);
							
							/** 发送请求 */
							HttpResponseEntity responseEntity = httpClinetService.sendPost(personinfoIp, dataMap, false);
							
							/** 判断返回状态码 */
							if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
								String content = responseEntity.getContent();
//								String str = "{\r\n" + 
//										"    \"transContent\": {\r\n" + 
//										"        \"authSrcId\": \"102\",\r\n" + 
//										"        \"failArr\": [\r\n" + 
//										"            {\r\n" + 
//										"                \"errorCode\": \"E000001\",\r\n" + 
//										"                \"errorMsg\": \"服务器证书签名验证失败\",\r\n" + 
//										"                \"reqOpt\": \"I\",\r\n" + 
//										"                \"sigCertSn\": \"100001036636567\"\r\n" + 
//										"            },\r\n" + 
//										"            {\r\n" + 
//										"                \"errorCode\": \"E000001\",\r\n" + 
//										"                \"errorMsg\": \"服务器证书签名验证失败\",\r\n" + 
//										"                \"reqOpt\": \"I\",\r\n" + 
//										"                \"sigCertSn\": \"100001036636567\"\r\n" + 
//										"            }\r\n" + 
//										"        ],\r\n" + 
//										"        \"failNum\": 1,\r\n" + 
//										"        \"resultState\": 0,\r\n" + 
//										"        \"totalNum\": 1\r\n" + 
//										"    }\r\n" + 
//										"}";
								
								/** 解析返回JSON数据 */
								Map<String, Object> readValue = objectMapper.readValue(content, Map.class);

								StringBuffer dataBuffer = new StringBuffer();
								/** PK签名值判断 */
							//	if (PkiUtils.checkSign(content, (String) readValue.get(TRANSSIGNEDVALUE))) {
									Map<String, Object> transContent = (Map<String, Object>) readValue.get(TRANSCONTENT);
									
									/** 返回数据内容 */
									if (transContent != null && transContent.size() > 0) {
										StringBuffer stringBuffer = new StringBuffer();
										stringBuffer.append(ZH_AUTHSRCID).append(transContent.get(AUTHSRCID))
												.append("</br>").append(ZH_RESULTSTATE);
										if ((Integer)transContent.get(RESULTSTATE) == 0) {
											stringBuffer.append(ZH_RESULTSTATE1).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 1) {
											stringBuffer.append(ZH_RESULTSTATE2).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 2) {
											stringBuffer.append(ZH_RESULTSTATE3).append("</br>");
										}

										stringBuffer.append(ZH_TOTALNUM).append(transContent.get(TOTALNUM))
												.append("</br>").append(ZH_FAILNUM).append(transContent.get(FAILNUM))
												.append("</br>");

										List<Map<String, Object>> failArrs = (List<Map<String, Object>>) transContent.get("failArr");
										if (failArrs != null && failArrs.size() > 0) {
											StringBuffer failArrsBuffer = new StringBuffer();
											for (int j = 0; j < failArrs.size(); j++) {
												if (j != 0) {
													failArrsBuffer.append("</br>");
												}
												failArrsBuffer.append(ZH_REQOPT);
												  Object reqOpt = failArrs.get(j).get(REQOPT);
												  if(reqOpt.equals("I")) {
													  failArrsBuffer.append(REQOPTADD).append("</br>");
												  }else if(reqOpt.equals("U")) {
													  failArrsBuffer.append(REQOPTUPDATE).append("</br>");
												  }else if(reqOpt.equals("D")) {
													  failArrsBuffer.append(REQOPTDELETE).append("</br>");
												  }
												  
												  failArrsBuffer.append(ZH_SIGCERTSN).append(failArrs.get(j).get(SIGCERTSN)).append("</br>")
																.append(ZH_ERRORCODE);
												Object errorCode = failArrs.get(j).get(ERRORCODE);
												if (errorCode.equals(ERRORCODENUM0)) {
													failArrsBuffer.append(ZH_ERRORCODENUM0).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM1)) {
													failArrsBuffer.append(ZH_ERRORCODENUM1).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM2)) {
													failArrsBuffer.append(ZH_ERRORCODENUM2).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM3)) {
													failArrsBuffer.append(ZH_ERRORCODENUM3).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM4)) {
													failArrsBuffer.append(ZH_ERRORCODENUM4).append("</br>");
												}
												failArrsBuffer.append(ZH_ERRORMSG);
												Object errorMsg = failArrs.get(j).get(ERRORMSG);
												if (errorMsg != null) {
													failArrsBuffer.append(failArrs.get(j).get(ERRORMSG));
												}
											}
											stringBuffer.append("</br>").append(failArrsBuffer.toString());
										}

										dataBuffer.append(stringBuffer.toString());
									}
								//} 
								buffers.add(dataBuffer);
							}

							/** 标题判断 */
						} else if (firstCellValue.equals(CAPERSON)) {

							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
							for (int i = 0; i < readExcel.size(); i++) {
								Map<String, Object> map = new LinkedHashMap<>();
								List<Object> model = readExcel.get(i);
								map.put("authSrcId", model.get(0));
								
								if(((String) model.get(1)).trim().equals("新增")) {
									map.put("reqOpt", "I");
								}else if(((String) model.get(1)).trim().equals("更新")) {
									map.put("reqOpt", "U");
								}else if(((String) model.get(1)).trim().equals("删除")) {
									map.put("reqOpt", "D");
								}
								map.put("sigCertSn", model.get(2));
								
								if(((String) model.get(3)).trim().equals("禁止访问")) {
									map.put("accessCtrlOpt", 0);
								}else if(((String) model.get(3)).trim().equals("允许访问")) {
									map.put("accessCtrlOpt", 1);
								}
								
								String object = (String) model.get(4);
								String[] split = object.split("|");
								for (int k = 0; k < file.length; k++) {
									if(k != 0) {
										if(split[k].trim().equals("省统一身份认证平台")) {
											map.put("appSysIds", "ASCODE10001|ASCODE10002");
										}else if(split[k].trim().equals("省公共资源")) {
											map.put("appSysIds", "ASCODE10002|ASCODE10001");
										}
									}else {
										if(split[k].trim().equals("省统一身份认证平台")) {
											map.put("appSysIds", "ASCODE10001");
										}else if(split[k].trim().equals("省公共资源")) {
											map.put("appSysIds", "ASCODE10002");
										}
									}
									
								}
								
								
								caPersons.add(map);
							}
							dataMap.put("transContent", caPersons);
							String sign = PkiUtils.sign(objectMapper.writeValueAsString(caPersons));
							dataMap.put("transSignedValue", sign);
							HttpResponseEntity responseEntity = httpClinetService.sendPost(appaccessctrlIp, dataMap, false);
							
							/** 判断返回状态码 */
							if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
								String content = responseEntity.getContent();
								/** 解析返回JSON数据 */
								Map<String, Object> readValue = objectMapper.readValue(content, Map.class);

								StringBuffer dataBuffer = new StringBuffer();
								/** PK签名值判断 */
								//if (PkiUtils.checkSign(content, (String) readValue.get(TRANSSIGNEDVALUE))) {
									Map<String, Object> transContent = (Map<String, Object>) readValue.get(TRANSCONTENT);
									
									/** 返回数据内容 */
									if (transContent != null && transContent.size() > 0) {
										StringBuffer stringBuffer = new StringBuffer();
										stringBuffer.append(ZH_AUTHSRCID).append(transContent.get(AUTHSRCID))
												.append("</br>").append(ZH_RESULTSTATE);
										if ((Integer)transContent.get(RESULTSTATE) == 0) {
											stringBuffer.append(ZH_RESULTSTATE1).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 1) {
											stringBuffer.append(ZH_RESULTSTATE2).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 2) {
											stringBuffer.append(ZH_RESULTSTATE3).append("</br>");
										}

										stringBuffer.append(ZH_TOTALNUM).append(transContent.get(TOTALNUM))
												.append("</br>").append(ZH_FAILNUM).append(transContent.get(FAILNUM))
												.append("</br>");

										List<Map<String, Object>> failArrs = (List<Map<String, Object>>) transContent.get("failArr");
										if (failArrs != null && failArrs.size() > 0) {
											StringBuffer failArrsBuffer = new StringBuffer();
											for (int j = 0; j < failArrs.size(); j++) {
												if (j != 0) {
													failArrsBuffer.append("</br>");
												}
												
											  failArrsBuffer.append(ZH_AUTHSRCID).append(failArrs.get(j).get(AUTHSRCID)).append("</br>")
															.append(ZH_REQOPT);
											  Object reqOpt = failArrs.get(j).get(REQOPT);
											  if(reqOpt.equals("I")) {
												  failArrsBuffer.append(REQOPTADD).append("</br>");
											  }else if(reqOpt.equals("U")) {
												  failArrsBuffer.append(REQOPTUPDATE).append("</br>");
											  }else if(reqOpt.equals("D")) {
												  failArrsBuffer.append(REQOPTDELETE).append("</br>");
											  }
											  
											  failArrsBuffer.append(ZH_SIGCERTSN).append(failArrs.get(j).get(SIGCERTSN)).append("</br>")
											  				.append(ZH_APPSYSIDS);
											  String appSysIds = (String) failArrs.get(j).get("appSysIds");
											  String[] split = appSysIds.split("|");
											  for (int i = 0; i < split.length; i++) {
												  if(i != 0) {
													  if(split[i].equals(APPSYSID1)) {
														  failArrsBuffer.append("|").append(ZH_APPSYSID1);
													  }else if(split[i].equals(APPSYSID2)) {
														  failArrsBuffer.append("|").append(ZH_APPSYSID2);
													  }
												  }else {
													  if(split[i].equals(APPSYSID1)) {
														  failArrsBuffer.append(ZH_APPSYSID1);
													  }else if(split[i].equals(APPSYSID2)) {
														  failArrsBuffer.append(ZH_APPSYSID2);
													  }
												  }
											  }
											  failArrsBuffer.append("<br>");
											  
											  failArrsBuffer.append(ZH_ERRORCODE);
												Object errorCode = failArrs.get(j).get(ERRORCODE);
												if (errorCode.equals(ERRORCODENUM0)) {
													failArrsBuffer.append(ZH_ERRORCODENUM0).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM1)) {
													failArrsBuffer.append(ZH_ERRORCODENUM1).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM2)) {
													failArrsBuffer.append(ZH_ERRORCODENUM2).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM3)) {
													failArrsBuffer.append(ZH_ERRORCODENUM3).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM4)) {
													failArrsBuffer.append(ZH_ERRORCODENUM4).append("</br>");
												}
												failArrsBuffer.append(ZH_ERRORMSG);
												Object errorMsg = failArrs.get(j).get(ERRORMSG);
												if (errorMsg != null) {
													failArrsBuffer.append(failArrs.get(j).get(ERRORMSG));
												}
											}
											stringBuffer.append("</br>").append(failArrsBuffer.toString());
										}

										dataBuffer.append(stringBuffer.toString());
									}
								//} 
								buffers.add(dataBuffer);
							}
							
							
							/** 标题判断 */
						} else if (firstCellValue.equals(CACREDENTIAL)) {

							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
							for (int i = 0; i < readExcel.size(); i++) {
								List<Object> model = readExcel.get(i);
								CaCredential caCredential = new CaCredential();
								caCredential.setAuthSrcId((String) model.get(0));
								
								if(((String) model.get(1)).trim().equals("新增")) {
									caCredential.setReqOpt("I");
								}else if(((String) model.get(1)).trim().equals("更新")) {
									caCredential.setReqOpt("U");
								}else if(((String) model.get(1)).trim().equals("删除")) {
									caCredential.setReqOpt("D");
								}
								
								caCredential.setCertChainId((String) model.get(2));
								caCredential.setCerChainFile((String) model.get(3));
								caCredentials.add(caCredential);
							}
							dataMap.put("transContent", caCredentials);
							dataMap.put("transSignedValue", PkiUtils.sign(objectMapper.writeValueAsString(caCredentials)));
							
							HttpResponseEntity responseEntity = httpClinetService.sendPost(certchainIp, dataMap, false);
							/** 判断返回状态码 */
							if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
								String content = responseEntity.getContent();
								/** 解析返回JSON数据 */
								Map<String, Object> readValue = objectMapper.readValue(content, Map.class);

								StringBuffer dataBuffer = new StringBuffer();
								/** PK签名值判断 */
								//if (PkiUtils.checkSign(objectMapper.writeValueAsString(readValue.get("transContent")), (String) readValue.get(TRANSSIGNEDVALUE))) {
									Map<String, Object> transContent = (Map<String, Object>) readValue.get(TRANSCONTENT);
									
									/** 返回数据内容 */
									if (transContent != null && transContent.size() > 0) {
										StringBuffer stringBuffer = new StringBuffer();
										stringBuffer.append(ZH_AUTHSRCID).append(transContent.get(AUTHSRCID))
												.append("</br>").append(ZH_RESULTSTATE);
										if ((Integer)transContent.get(RESULTSTATE) == 0) {
											stringBuffer.append(ZH_RESULTSTATE1).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 1) {
											stringBuffer.append(ZH_RESULTSTATE2).append("</br>");
										} else if ((Integer)transContent.get(RESULTSTATE) == 2) {
											stringBuffer.append(ZH_RESULTSTATE3).append("</br>");
										}

										stringBuffer.append(ZH_TOTALNUM).append(transContent.get(TOTALNUM))
												.append("</br>").append(ZH_FAILNUM).append(transContent.get(FAILNUM))
												.append("</br>");

										List<Map<String, Object>> failArrs = (List<Map<String, Object>>) transContent.get("failArr");
										if (failArrs != null && failArrs.size() > 0) {
											StringBuffer failArrsBuffer = new StringBuffer();
											for (int j = 0; j < failArrs.size(); j++) {
												if (j != 0) {
													failArrsBuffer.append("</br>");
												}
												
											  failArrsBuffer.append(ZH_REQOPT);
											  Object reqOpt = failArrs.get(j).get(REQOPT);
											  if(reqOpt.equals("I")) {
												  failArrsBuffer.append(REQOPTADD).append("</br>");
											  }else if(reqOpt.equals("U")) {
												  failArrsBuffer.append(REQOPTUPDATE).append("</br>");
											  }else if(reqOpt.equals("D")) {
												  failArrsBuffer.append(REQOPTDELETE).append("</br>");
											  }
											  
											  failArrsBuffer.append(ZH_AUTHSRCID).append(failArrs.get(j).get(AUTHSRCID)).append("</br>")
											  				.append(ZH_CERTCHAINID).append(failArrs.get(j).get(CERTCHAINID)).append("</br>")
															.append(ZH_ERRORCODE);
												Object errorCode = failArrs.get(j).get(ERRORCODE);
												if (errorCode.equals(ERRORCODENUM0)) {
													failArrsBuffer.append(ZH_ERRORCODENUM0).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM1)) {
													failArrsBuffer.append(ZH_ERRORCODENUM1).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM2)) {
													failArrsBuffer.append(ZH_ERRORCODENUM2).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM3)) {
													failArrsBuffer.append(ZH_ERRORCODENUM3).append("</br>");
												} else if (errorCode.equals(ERRORCODENUM4)) {
													failArrsBuffer.append(ZH_ERRORCODENUM4).append("</br>");
												}
												failArrsBuffer.append(ZH_ERRORMSG);
												Object errorMsg = failArrs.get(j).get(ERRORMSG);
												if (errorMsg != null) {
													failArrsBuffer.append(failArrs.get(j).get(ERRORMSG));
												}
											}
											stringBuffer.append("</br>").append(failArrsBuffer.toString());
										}

										dataBuffer.append(stringBuffer.toString());
									}
								//} 
								buffers.add(dataBuffer);
							}
							
						} else {
							/** 文件类型不匹配 */
							return new Result(false, "不是需要的文件的类型！");
						}

						StringBuffer responeBuffer = new StringBuffer();
						if (buffers != null && buffers.size() > 0) {
							for (int i = 0; i < buffers.size(); i++) {
								if (i != 0) {
									responeBuffer.append("\n");
								}
								responeBuffer.append(buffers.get(i).toString());
							}
						}

						/** 文件上传成功 */
						return new Result(true, responeBuffer.toString());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Result(false);
		}
		return null;
	}
}
