package com.npx.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.npx.admin.pojo.CaCredential;
import com.npx.admin.pojo.CaPerson;
import com.npx.admin.pojo.HttpResponseEntity;
import com.npx.admin.pojo.OrgCredential;
import com.npx.admin.pojo.OrgPersonCredential;
import com.npx.admin.utils.HttpClientService;
import com.npx.admin.utils.ObjectTOMapUtils;

/**
 * @ClassName: DataReceiverController  
 * @Description: 证书数据接收控制层  
 * @author NiePengXiang
 * @date 2018年7月8日  
 */
@Controller
@RequestMapping("/data")
public class DataReceiverController {
	
	Logger logger = LoggerFactory.getLogger(DataReceiverController.class);
	
	@Autowired
	@Qualifier("httpClientService")
	private HttpClientService httpClinetService;
	
	/**
	 * @Title: getOrgCredential  
	 * @Description:机构证书接收数据调用外部服务   
	 * @param  orgCredential
	 * @return String    
	 */
	@ResponseBody
	@PostMapping(value = "/orgCredential", produces = { "application/json;charse=UTF-8" })
	public String getOrgCredential(@RequestBody OrgCredential orgCredential) {
		
		try {
			
			Map<String,Object> dataMap = new HashMap<>();
			orgCredential.setSigCer(Base64Utils.encodeToString(orgCredential.getSigCer().getBytes()));
			orgCredential.setEncCer(Base64Utils.encodeToString(orgCredential.getEncCer().getBytes()));
			dataMap.put("transContent", orgCredential);
			HttpResponseEntity responseEntity = httpClinetService.sendPost("", ObjectTOMapUtils.obj2Map(dataMap),
					false);
			if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
				return responseEntity.getContent();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.info("发送机构证书数据失败");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @Title: getOrgPersonCredential  
	 * @Description:个人非机构接收数据调用外部服务     
	 * @param  orgPersonCredential
	 * @return String    返回类型  
	 */
	@ResponseBody
	@PostMapping(value = "/orgPersonCredential", produces = { "application/json;charse=UTF-8" })
	public String getOrgPersonCredential(@RequestBody OrgPersonCredential orgPersonCredential) {
		try {
			
			Map<String,Object> dataMap = new HashMap<>();
			orgPersonCredential.setSigCer(Base64Utils.encodeToString(orgPersonCredential.getSigCer().getBytes()));
			orgPersonCredential.setEncCer(Base64Utils.encodeToString(orgPersonCredential.getEncCer().getBytes()));
			dataMap.put("transContent", orgPersonCredential);
			
			HttpResponseEntity responseEntity = httpClinetService.sendPost("",
					ObjectTOMapUtils.obj2Map(dataMap), false);

			if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
				return responseEntity.getContent();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.info("发送个人非机构证书数据失败");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @Title: getCaCredential  
	 * @Description: CA证书链接收数据调用外部服务   
	 * @param  caCredential
	 * @return String
	 */
	@ResponseBody
	@PostMapping(value = "/caCredential", produces = { "application/json;charse=UTF-8" })
	public String getCaCredential(@RequestBody CaCredential caCredential) {
		try {

			Map<String,Object> dataMap = new HashMap<>();
			
			dataMap.put("transContent", caCredential);
			
			HttpResponseEntity responseEntity = httpClinetService.sendPost("", ObjectTOMapUtils.obj2Map(dataMap),
					false);

			if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
				return responseEntity.getContent();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.info("发送个CA用户证书数据失败");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @Title: getCaPerson  
	 * @Description: CA用户证书接收数据调用外部服务   
	 * @param  caPerson
	 * @return String   
	 */
	@ResponseBody
	@PostMapping(value = "/caPerson", produces = { "application/json;charse=UTF-8" })
	public String getCaPerson(@RequestBody CaPerson caPerson) {
		try {
			
			Map<String,Object> dataMap = new HashMap<>();
			
			
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
			HttpResponseEntity responseEntity = httpClinetService.sendPost("", dataMap, false);

			if (responseEntity.getStatusCode() == HttpStatus.SC_OK) {
				return responseEntity.getContent();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.info("发送个CA证书链数据失败");
			throw new RuntimeException(e);
		}
	}
}
