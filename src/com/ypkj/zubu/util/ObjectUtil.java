package com.ypkj.zubu.util;

import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;


public class ObjectUtil {

	public static Map<String,  Object> stringToMap(String jsonStr) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		return objectMapper.readValue(jsonStr, Map.class);
	}
}
