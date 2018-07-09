package com.npx.admin.controller;

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
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	public Result importExcel(@RequestParam(value = "excelFile", required = false) MultipartFile[] file,
			HttpServletRequest request) {
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

						/** 标题判断 */
						if (firstCellValue.equals(ORGCREDENTIAL)) {

							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
							for (int i = 0; i < readExcel.size(); i++) {

								OrgCredential orgCredential = new OrgCredential();
								List<Object> model = readExcel.get(i);
								orgCredential.setAuthSrcId((String) model.get(0));
								orgCredential.setReqOpt((String) model.get(1));
								orgCredential.setSigCertSn((String) model.get(2));
								orgCredential.setEncCertSn((String) model.get(3));
								orgCredential.setSigCer(Base64Utils.encodeToString(((String) model.get(4)).getBytes()));
								orgCredential.setEncCer(Base64Utils.encodeToString(((String) model.get(5)).getBytes()));
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

							/** 标题判断 */
						} else if (firstCellValue.equals(ORGPERSONCREDENTIAL)) {

							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
							for (int i = 0; i < readExcel.size(); i++) {
								OrgPersonCredential orgPersonCredential = new OrgPersonCredential();
								List<Object> model = readExcel.get(i);
								orgPersonCredential.setAuthSrcId((String) model.get(0));
								orgPersonCredential.setReqOpt((String) model.get(1));
								orgPersonCredential.setSigCertSn((String) model.get(2));
								orgPersonCredential.setEncCertSn((String) model.get(3));
								orgPersonCredential
										.setSigCer(Base64Utils.encodeToString(((String) model.get(4)).getBytes()));
								orgPersonCredential
										.setEncCer(Base64Utils.encodeToString(((String) model.get(5)).getBytes()));
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

							/** 标题判断 */
						} else if (firstCellValue.equals(CAPERSON)) {

							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
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
							/** 标题判断 */
						} else if (firstCellValue.equals(CACREDENTIAL)) {

							List<List<Object>> readExcel = ReadExcelTools.readCellsValue(excelFile.getInputStream(),
									fileName);
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
						} else {

							/** 文件类型不匹配 */
							return new Result(false, "不是需要的文件的类型！");
						}

						/** 文件上传成功 */
						return new Result(true);
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
