package com.npx.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npx.admin.pojo.CaCredential;
import com.npx.admin.pojo.CaPerson;
import com.npx.admin.pojo.HttpResponseEntity;
import com.npx.admin.pojo.OrgCredential;
import com.npx.admin.pojo.OrgPersonCredential;
import com.npx.admin.utils.HttpClientService;
import com.npx.admin.utils.ObjectTOMapUtils;
import com.npx.admin.utils.PkiUtils;

/**
 * @ClassName: DataReceiverController
 * @Description: 证书数据接收控制层
 * @author NiePengXiang
 * @date 2018年7月8日
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/data")
public class DataReceiverController {

	Logger logger = LoggerFactory.getLogger(DataReceiverController.class);

	@Autowired
	@Qualifier("httpClientService")
	private HttpClientService httpClinetService;

	@Autowired
	@Qualifier("objectMapper")
	private ObjectMapper objectMapper;

	@Value("${orginfoIp}")
	private String orginfoIp;
	
	@Value("${personinfoIp}")
	private String personinfoIp;
	
	@Value("${appaccessctrlIp}")
	private String appaccessctrlIp;
	
	@Value("${certchainIp}")
	private String certchainIp;

	/**
	 * @Title: getOrgCredential
	 * @Description:机构证书接收数据调用外部服务
	 * @param orgCredential
	 * @return String
	 */
	@ResponseBody
	@PostMapping(value = "/orgCredential", produces = { "application/json;charset=UTF-8" })
	public String getOrgCredential(@RequestBody OrgCredential orgCredential) {

		try {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("transContent", orgCredential);
			
			/**私钥内容加密*/
			String sign = PkiUtils.sign(objectMapper.writeValueAsString(orgCredential));
			dataMap.put("transSignedValue", sign);
			
			HttpResponseEntity responseEntity = httpClinetService.sendPost(orginfoIp, ObjectTOMapUtils.obj2Map(dataMap),
					false);
			return returnJson(responseEntity);
		} catch (Exception e) {
			logger.info("发送机构证书数据失败");
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Title: getOrgPersonCredential
	 * @Description:个人非机构接收数据调用外部服务
	 * @param orgPersonCredential
	 * @return String 返回类型
	 */
	@ResponseBody
	@PostMapping(value = "/orgPersonCredential", produces = { "application/json;charset=UTF-8" })
	public String getOrgPersonCredential(@RequestBody OrgPersonCredential orgPersonCredential) {
		try {

			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("transContent", orgPersonCredential);
			/**私钥内容加密*/
			String sign = PkiUtils.sign(objectMapper.writeValueAsString(orgPersonCredential));
			dataMap.put("transSignedValue", sign);

			HttpResponseEntity responseEntity = httpClinetService.sendPost(personinfoIp, ObjectTOMapUtils.obj2Map(dataMap),false);
			return returnJson(responseEntity);
		} catch (Exception e) {
			logger.info("发送个人非机构证书数据失败");
			throw new RuntimeException(e);
		}
	}


	/**
	 * @Title: getCaCredential
	 * @Description: CA证书链接收数据调用外部服务
	 * @param caCredential
	 * @return String
	 */
	@ResponseBody
	@PostMapping(value = "/caCredential", produces = { "application/json;charset=UTF-8" })
	public String getCaCredential(@RequestBody CaCredential caCredential) {
		try {

			Map<String, Object> dataMap = new HashMap<>();

			dataMap.put("transContent", caCredential);
			/**私钥内容加密*/
			String sign = PkiUtils.sign(objectMapper.writeValueAsString(caCredential));
			dataMap.put("transSignedValue", sign);


			HttpResponseEntity responseEntity = httpClinetService.sendPost(appaccessctrlIp, ObjectTOMapUtils.obj2Map(dataMap),false);

			return returnJson(responseEntity);
		} catch (Exception e) {
			logger.info("发送个CA用户证书数据失败");
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Title: getCaPerson
	 * @Description: CA用户证书接收数据调用外部服务
	 * @param caPerson
	 * @return String
	 */
	@ResponseBody
	@PostMapping(value = "/caPerson", produces = { "application/json;charset=UTF-8" })
	public String getCaPerson(@RequestBody CaPerson caPerson) {
		try {

			Map<String, Object> dataMap = new HashMap<>();

			List<String> appSysIds = caPerson.getAppSysIds();
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < appSysIds.size(); i++) {
				if (i == 1) {
					stringBuffer.append("|").append(appSysIds.get(i));
				}
				stringBuffer.append(appSysIds.get(i));
			}
			Map<String, Object> map = new HashMap<>();

			map.put("authSrcId", stringBuffer.toString());
			map.put("reqOpt", caPerson.getReqOpt());
			map.put("sigCertSn", caPerson.getSigCertSn());
			map.put("accessCtrlOpt", caPerson.getAccessCtrlOpt());
			map.put("appSysIds", caPerson.getAuthSrcId());

			dataMap.put("transContent", map);
			/**私钥内容加密*/
			String sign = PkiUtils.sign(objectMapper.writeValueAsString(map));
			dataMap.put("transSignedValue", sign);
			HttpResponseEntity responseEntity = httpClinetService.sendPost(certchainIp, dataMap, false);

			return returnJson(responseEntity);
			
		} catch (Exception e) {
			logger.info("发送个CA证书链数据失败");
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * @Title: returnJson  
	 * @Description: 返回请求响应JSON数据
	 * @param @param responseEntity
	 * @param  IOException
	 * @param  JsonParseException
	 * @param  JsonMappingException
	 * @param  Exception
	 * @param  JsonProcessingException   
	 * @return String    返回类型  
	 * @throws Exception 
	 * @throws JsonProcessingException 
	 */
	private String returnJson(HttpResponseEntity responseEntity) throws JsonProcessingException, Exception{
		if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
			String content = responseEntity.getContent();
			//String content = 
//					"{\r\n" + 
//					"    \"transContent\": {\r\n" + 
//					"        \"authSrcId\": \"102\",\r\n" + 
//					"        \"failArr\": [\r\n" + 
//					"            {\r\n" + 
//					"                \"errorCode\": \"E000001\",\r\n" + 
//					"                \"errorMsg\": \"服务器证书签名验证失败\",\r\n" + 
//					"                \"reqOpt\": \"I\",\r\n" + 
//					"                \"sigCertSn\": \"100001036636567\"\r\n" + 
//					"            }\r\n" + 
//					"        ],\r\n" + 
//				    "        \"failNum\": 1,\r\n" + 
//				    "        \"resultState\": \"0\",\r\n" + 
//					"        \"totalNum\": 1\r\n" + 
//					"    }\r\n" + 
//					"}";
//					"{\r\n" + 
//					"    \"transContent\": {\r\n" + 
//					"        \"authSrcId\": \"102\",\r\n" + 
//					"        \"failArr\": [\r\n" + 
//					"            {\r\n" + 
//					"                \"appSysIds\": \"ASCODE10001|ASCODE10002\",\r\n" + 
//					"                \"authSrcId\": \"102\",\r\n" + 
//					"                \"errorCode\": \"E000001\",\r\n" + 
//					"                \"errorMsg\": \"服务器证书签名验证失败\",\r\n" + 
//					"                \"reqOpt\": \"I\",\r\n" + 
//					"                \"sigCertSn\": \"100001036636567\"\r\n" + 
//					"            }\r\n" + 
//					"        ],\r\n" + 
//					"        \"failNum\": 1,\r\n" + 
//					"        \"resultState\": 0,\r\n" + 
//					"        \"totalNum\": 1\r\n" + 
//					"    }\r\n" + 
//					"}";
			
			Map<String, Object> readValue = objectMapper.readValue(content, Map.class);
			Map<String, Object> responseMap = (Map<String, Object>) readValue.get("transContent");
			if (PkiUtils.checkSign(objectMapper.writeValueAsString(responseMap),
					(String) readValue.get("transSignedValue"))) {
				return objectMapper.writeValueAsString(responseMap);
			}else {
				logger.error("证书签名值不匹配");
				throw new RuntimeException("消息不匹配");
			}
		} else {
			logger.error("请求返回状态码错误");
			throw new RuntimeException("请求返回状态码错误");
		}
	}
}
