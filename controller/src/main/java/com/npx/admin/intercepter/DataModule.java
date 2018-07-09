package com.npx.admin.intercepter;

public interface DataModule {
	
	public static final String ORGCREDENTIAL = "机构证书用户信息同步";
	public static final String ORGPERSONCREDENTIAL = "个人非机构证书用户信息同步";
	public static final String CAPERSON = "CA用户证书信息同步";
	public static final String CACREDENTIAL = "CA证书链信息同步";
	
	/**返回JSON内容*/
	public static final String TRANSCONTENT = "transContent";
	
	/**返回JSON签名值*/
	public static final String TRANSSIGNEDVALUE = "transSignedValue";
	
	
	public static final String ZH_AUTHSRCID = "认证源ID：";
	public static final String AUTHSRCID = "authSrcId";
	
	public static final String ZH_RESULTSTATE = "同步结果：";
	public static final String RESULTSTATE = "resultstate";
	public static final String ZH_RESULTSTATE1 = "有部分数据同步失败!";
	public static final String ZH_RESULTSTATE2 = "所有都同步成功!";
	public static final String ZH_RESULTSTATE3 = "参数不正确，传入参数名或值有误!";
	
	
	public static final String ZH_TOTALNUM = "接收的总条数：";
	public static final String TOTALNUM = "totalNum";
	
	public static final String ZH_FAILNUM = "处理错误条数：";
	public static final String FAILNUM = "failNum";
	
	public static final String ZH_REQOPT = "请求操作：";
	public static final String REQOPT = "reqOpt";
	
	public static final String ZH_SIGCERTSN = "数字证书序列号：";
	public static final String SIGCERTSN = "sigCertSn";
	
	public static final String ZH_ERRORCODE = "实名认证错误编码：";
	public static final String ERRORCODE = "errorCode";
	
	
	public static final String ZH_ERRORCODENUM0 = "未知异常";
	public static final String ZH_ERRORCODENUM1 = "服务器证书签名验证失败";
	public static final String ZH_ERRORCODENUM2 = "字段不能为空";
	public static final String ZH_ERRORCODENUM3 = "唯一标识的值已存在，不能执行新增操作。";
	public static final String ZH_ERRORCODENUM4 = "唯一标识的值不存在，不能执行更新或删除操作。";
	public static final String ERRORCODENUM0 = "E000000";
	public static final String ERRORCODENUM1 = "E000001";
	public static final String ERRORCODENUM2 = "E000002";
	public static final String ERRORCODENUM3 = "E000003";
	public static final String ERRORCODENUM4 = "E000004";
	
	public static final String ZH_ERRORMSG = "错误信息：";
	public static final String ERRORMSG = "ERRORMSG";
	
}
