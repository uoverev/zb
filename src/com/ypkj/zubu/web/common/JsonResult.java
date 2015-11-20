package com.ypkj.zubu.web.common;

import java.util.HashMap;
import java.util.Map;


/**
 * JSON Response Basic data
 * 
 * @author tangqiang
 * 
 */
public class JsonResult {

	private int code = 100;

	private String message = "";
	
	private Map<String, Object> data = new HashMap<String, Object>();

	public JsonResult(){};
	
	public JsonResult(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public void appendData(String key, Object value) {
		this.data.put(key, value);
	}

	public void appendData(Map<String, Object> m) {
		this.data.putAll(m);
	}

}
