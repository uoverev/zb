package com.ypkj.zubu.util;

public class ResultCodeUtil {

 	/***成功-100 */
	public static final int SUCCESS_100 = 100;
	public static final String SUCCESS_100_MSG = "成功调用";
 	/*** 服务器异常-200*/
 	public static final int ERROR_200 = -200; 
 	public static final String ERROR_200_MSG = "服务器异常";
 	/***接口编号不存在-300  */
 	public static final int CODE_NOT_EXIST_300 = -300;
 	/*** 接口私钥错误-400 */
 	public static final int PRIVATE_KET_400 = -400; 
 	/*** 自定义错误-600 */
 	public static final int CUSTOM_ERROR_600 = -600; 
 	public static final String CUSTOM_ERROR_600_MSG = "自定义错误";
 	/*** 参数不合法-700 */
 	public static final int PARAMETER_NOT_VALID_700 = -700; 
 	public static final String PARAMETER_NOT_VALID_700_MSG = "参数不合法"; 
 	
}
