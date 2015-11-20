package com.ypkj.zubu.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应答工具类
 * 
 * @author zli
 *
 */
public final class ResponseUtils {
	/**
	 * 封装后台返回的信息
	 * 
	 * @param code
	 *            应答码
	 * @param msg
	 *            应答消息
	 * @param data
	 *            data数据
	 * @return
	 */
	public static Map<String, Object> setResponse(int code, String msg,
			List<Map<String, Object>> data) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("code", code);
		response.put("msg", msg);
		response.put("result", data);
		return response;
	}
}
