package com.ypkj.zubu.controller.app;

import java.util.HashMap;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.ypkj.zubu.util.Tools;

/**
 * 发送短信服务
 * 
 * @author Administrator
 *
 */
public class SendSMS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendMsg("15178863072");
	}

	@SuppressWarnings("unchecked")
	public static String sendMsg(String phoneNumbers) {
		HashMap<String, Object> result = null;

		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
		restAPI.init("app.cloopen.com", "8883");// 服务器地址
		restAPI.setAccount("8a48b5514f49079e014f4a6a593605dc",
				"2f0fa1fc64a940ed9c5ce9caf2eb5d9d");// 帐号
		restAPI.setAppId("8a48b5515018a0f401501c0a5b3605b5");// 密码

		// ******************************注释****************************************************************
		// *调用发送模板短信的接口发送短信 *
		// *参数顺序说明： *
		// *第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号 *
		// *第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。 *
		// *系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		// *第三个参数是要替换的内容数组。 *
		// **************************************************************************************************

		// **************************************举例说明***********************************************************************
		// *假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为
		// *
		// *result = restAPI.sendTemplateSMS("13800000000","1" ,new
		// String[]{"6532","5"}); *
		// *则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入 *
		// *********************************************************************************************************************

		// 产生随机四位验证码
		String code = String.valueOf(Tools.getRandomCode());
		result = restAPI.sendTemplateSMS(phoneNumbers, "40009", new String[] {
				code, "1" });

		System.out.println("SDKTestGetSubAccounts result=" + result);

		
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result
					.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				System.out.println(key + " = " + object);
			}
		} else {
			
			// 异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") + " 错误信息= "
					+ result.get("statusMsg"));
		}
		return code;
	}

}
