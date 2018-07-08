package com.npx.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.npx.admin.pojo.CaCredential;
import com.npx.admin.pojo.OrgCredential;
import com.npx.admin.pojo.OrgPersonCredential;
import com.npx.admin.pojo.Result;
import com.npx.admin.utils.HttpClientService;
import com.npx.admin.utils.ReadExcelTools;

@Controller
public class FileUploadController {
	private static final String ORGCREDENTIAL = "机构证书用户信息同步";
	private static final String ORGPERSONCREDENTIAL = "个人非机构证书用户信息同步";
	private static final String CAPERSON = "CA用户证书信息同步";
	private static final String CACREDENTIAL = "CA证书链信息同步";
	
	Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	@Qualifier("httpClientService")
	private HttpClientService httpClinetService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Result importExcel(@RequestParam(value = "excelFile", required = false) MultipartFile file,
			HttpServletRequest request) throws IOException, InterruptedException {
		try {
			MultipartRequest multipartRequest = (MultipartRequest) request;
			MultipartFile excelFile = multipartRequest.getFile("excelFile");
			
			Map<String, Object> dataMap = new HashMap<>();
			List<OrgCredential> credentials = new ArrayList<>();
			List<OrgPersonCredential> orgPersonCredentials = new ArrayList<>();
			List<Map<String, Object>> caPersons = new ArrayList<>();
			List<CaCredential> caCredentials = new ArrayList<>();
			if (excelFile != null) {
				String fileName = excelFile.getOriginalFilename();

				String firstCellValue = (String) ReadExcelTools.getFirstCellValue(excelFile.getInputStream(), fileName);
				if (firstCellValue.equals(ORGCREDENTIAL)) {

					List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(), fileName);
					for (int i = 0; i < readExcel.size(); i++) {
						OrgCredential orgCredential = new OrgCredential();
						List<Object> model = readExcel.get(i);
						orgCredential.setAuthSrcId((String) model.get(0));
						orgCredential.setReqOpt((String) model.get(1));
						orgCredential.setSigCertSn((String) model.get(2));
						orgCredential.setEncCertSn((String) model.get(3));
						orgCredential.setSigCer((String) model.get(4));
						orgCredential.setEncCer((String) model.get(5));
						orgCredential.setCertStatus(Integer.parseInt((String) model.get(6)));
						orgCredential.setCertIssuer((String) model.get(7));
						orgCredential.setCertUserTypeID((String) model.get(8));
						orgCredential.setOrgCodeType((String) model.get(9));
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

					httpClinetService.sendPost(null, dataMap, false);

				}
				if (firstCellValue.equals(ORGPERSONCREDENTIAL)) {

					List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(), fileName);
					for (int i = 0; i < readExcel.size(); i++) {
						OrgPersonCredential orgPersonCredential = new OrgPersonCredential();
						List<Object> model = readExcel.get(i);
						orgPersonCredential.setAuthSrcId((String) model.get(0));
						orgPersonCredential.setReqOpt((String) model.get(1));
						orgPersonCredential.setSigCertSn((String) model.get(2));
						orgPersonCredential.setEncCertSn((String) model.get(3));
						orgPersonCredential.setSigCer((String) model.get(4));
						orgPersonCredential.setEncCer((String) model.get(5));
						orgPersonCredential.setCertStatus(Integer.parseInt((String) model.get(6)));
						orgPersonCredential.setCertIssuer((String) model.get(7));
						orgPersonCredential.setUserName((String) model.get(8));
						orgPersonCredential.setCertUserTypeID((String) model.get(9));
						orgPersonCredential.setSex((Integer) model.get(10));
						orgPersonCredential.setCredentialType((String) model.get(11));
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

					httpClinetService.sendPost(null, dataMap, false);

				}
				if (firstCellValue.equals(CAPERSON)) {

					List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(), fileName);
					for (int i = 0; i < readExcel.size(); i++) {
						Map<String, Object> map = new LinkedHashMap<>();
						List<Object> model = readExcel.get(i);
						map.put("authSrcId", model.get(0));
						map.put("reqOpt", model.get(1));
						map.put("sigCertSn", model.get(2));
						map.put("accessCtrlOpt", model.get(3));
						map.put("appSysIds", model.get(4));
						caPersons.add(map);
					}
					dataMap.put("transContent", caPersons);

					httpClinetService.sendPost(null, dataMap, false);

				}
				
				if(firstCellValue.equals(CACREDENTIAL)) {
					
					List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(), fileName);
					for (int i = 0; i < readExcel.size(); i++) {
						List<Object> model = readExcel.get(i);
						CaCredential caCredential = new CaCredential();
						caCredential.setAuthSrcId((String) model.get(0));
						caCredential.setReqOpt((String) model.get(1));
						caCredential.setCertChainId((String) model.get(2));
						caCredential.setCerChainFile((String) model.get(3));
						caCredentials.add(caCredential);
					}
					dataMap.put("transContent", caCredentials);
					httpClinetService.sendPost(null, dataMap, false);
				}

				return new Result(true);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Result(false);
		}
		return null;
	}
}
