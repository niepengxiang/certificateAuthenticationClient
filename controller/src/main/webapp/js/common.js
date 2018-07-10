$.fn.serializeObject = function() {
	var json = {};
	var map = this.serializeArray();
	$.each(map, function() {
		if (json[this.name]) {
			if (!json[this.name].push) {
				json[this.name] = [ json[this.name] ];
			}
			json[this.name].push(this.value || '');
		} else {
			json[this.name] = this.value || '';
		}
	});
	return json;
};

function returnStr(msg,flag){
	var htmlStr = "";
	var authSrcId = msg.authSrcId;
	var resultState = msg.resultState;
	var totalNum = msg.totalNum;
	var failNum = msg.failNum;
	if(resultState == 0){
		resultStateStr = "有部分数据同步失败";
	}else if(resultState == 1){
		resultStateStr = "所有同步成功";
	}else if(resultState == 2){
		resultStateStr = "参数不正确";
	}
	
	var failArr = msg.failArr;
	
	if(failArr){
		var reqOpt = "";
		var sigCertSn = "";
		var errorCode = "";
		var appSysIds = "";
		var errorMsg = "";
		if(flag == "org"){
			$.each(failArr,function(index,value){
				if(value.reqOpt == "I"){
					reqOpt="新增";
				}else if(value.reqOpt == "U"){
					reqOpt="更新";
				}else if(value.reqOpt == "D"){
					reqOpt="删除";
				}
				sigCerSn = value.sigCertSn;
				errorCode = value.errorCode;
				errorMsg = value.errorMsg;
			});
			
			var errorCodeMsg = returnErrorCodeMsg(errorCode);
			
			  
				  htmlStr = "认证源ID:" + authSrcId + "</br>"+	  
						  	"同步结果：" +  resultState + "</br>" +
						  	"接收的总条数：" + totalNum + "</br>" +
						  	"处理错误的条数：" + failNum + "</br>" +
						  	"请求操作：" + reqOpt + "</br>" +
						  	"数字证书序列号：" + sigCertSn + "</br>" +
							"实名认证错误编码：" + errorCode +"==="+ errorCodeMsg + "</br>" +
							"错误信息：" + 	errorMsg + "</br>"
		  } else if (flag == "caPerson"){
				$.each(failArr,function(index,value){
					if(value.reqOpt == "I"){
						reqOpt="新增";
					}else if(value.reqOpt == "U"){
						reqOpt="更新";
					}else if(value.reqOpt == "D"){
						reqOpt="删除";
					}
					sigCertSn = value.sigCertSn;
					errorCode = value.errorCode;
					errorMsg = value.errorMsg;
					var appSysIdsArr = value.appSysIds.split("|");
					for(var i = 0; i < appSysIdsArr.length; i++){
						if(i != 0){
							if(appSysIdsArr[i] == "ASCODE10001"){
								appSysIds = appSysIds + "|省统一身份认证平台";
							} else if (appSysIdsArr[i] == "ASCODE10002"){
								appSysIds = appSysIds + "|省公共资源";
							}
						}else{
							if(appSysIdsArr[i] == "ASCODE10001"){
								appSysIds = "省统一身份认证平台";
							}else if (appSysIdsArr[i] == "ASCODE10002"){
								appSysIds = "省公共资源";
							}
						}
						
					}
					
				});
				
				var errorCodeMsg = returnErrorCodeMsg(errorCode);
					  htmlStr = "认证源ID:" + authSrcId + "</br>"+	  
							  	"同步结果：" +  resultState + "</br>" +
							  	"接收的总条数：" + totalNum + "</br>" +
							  	"处理错误的条数：" + failNum + "</br>" +
							  	"请求操作：" + reqOpt + "</br>" +
							  	"数字证书序列号：" + sigCertSn + "</br>" +
							  	"访问控制应用系统：" + appSysIds + "</br>" + 
								"实名认证错误编码：" + errorCode +"==="+ errorCodeMsg + "</br>" +
								"错误信息：" + 	errorMsg + "</br>"
			  
		  } else if (flag = "caCredential"){
			  	var certChainId = "";
				$.each(failArr,function(index,value){
					if(value.reqOpt == "I"){
						reqOpt="新增";
					}else if(value.reqOpt == "U"){
						reqOpt="更新";
					}else if(value.reqOpt == "D"){
						reqOpt="删除";
					}
					sigCerSn = value.sigCertSn;
					errorCode = value.errorCode;
					errorMsg = value.errorMsg;
					certChainId = value.certChainId;
				});
				
				var errorCodeMsg = returnErrorCodeMsg(errorCode);
				
					  htmlStr = "认证源ID:" + authSrcId + "</br>"+	  
							  	"同步结果：" +  resultState + "</br>" +
							  	"接收的总条数：" + totalNum + "</br>" +
							  	"处理错误的条数：" + failNum + "</br>" +
							  	"请求操作：" + reqOpt + "</br>" +
							  	"数字证书序列号：" + certChainId + "</br>" +
								"实名认证错误编码：" + errorCode +"==="+ errorCodeMsg + "</br>" +
								"错误信息：" + 	errorMsg + "</br>"
			  
		  }
	}else{
		htmlStr = 	"认证源ID:" + authSrcId + "</br>"+	  
					"同步结果：" +  resultState + "</br>" +
					"接收的总条数" + totalNum + "</br>" +
					"处理错误的条数" + failNum + "</br>"
	}
	return htmlStr;
}





function returnErrorCodeMsg(errorCd){
	var errorCodeMsg = "";
	if(errorCd == "E000000"){
		errorCodeMsg = "未知异常";
	}else if(errorCd = "E000001"){
		errorCodeMsg = "服务器证书签名验证失败";
	}else if(errorCd = "E000002"){
		errorCd = "字段不能为空";
	}else if(errorCode = "E000003"){
		errorCodeMsg = "唯一标识的值已存在，不能执行新增操作。";
	}else if(errorCd = "E000004"){
		errorCodeMsg = "唯一标识的值不存在，不能执行更新或删除操作。";
	}
	
	return errorCodeMsg;
}