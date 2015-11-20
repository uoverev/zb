package com.ypkj.zubu.service.cash;

public class Zfbstate {
	//收款账户实名认证信息不完整，无法收款
	public static  String ERROR_OTHER_CERTIFY_LEVEL_LIMIT="ERROR_OTHER_CERTIFY_LEVEL_LIMIT"; 
	/** 用户状态不正确*/
 	public  static  String ILLEGAL_USER_STATUS="ILLEGAL_USER_STATUS";
	/** 收款用户不存在*/
 	public static  String  RECEIVE_USER_NOT_EXIST="RECEIVE_USER_NOT_EXIST";
	/** 收款账户尚未实名认证，无法收款*/
 	public static  String  ERROR_OTHER_NOT_REALNAMED="ERROR_OTHER_NOT_REALNAMED";
 	/** 用户姓名和收款名称不匹配*/
 	public static  String  ACCOUN_NAME_NOT_MATCH="ACCOUN_NAME_NOT_MATCH";
 	/** 收款金额格式有误*/
 	public static  String  RECEIVE_MONEY_ERROR="RECEIVE_MONEY_ERROR";
 	/** 用户逾期 15 天未复核，批次失败*/
 	public static  String  SYSTEM_DISUSE_FILE="SYSTEM_DISUSE_FILE";
}
