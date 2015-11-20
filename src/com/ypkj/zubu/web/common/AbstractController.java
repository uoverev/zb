package com.ypkj.zubu.web.common;

import com.ypkj.zubu.util.ResultCodeUtil;

public abstract class AbstractController {

	public JsonResult error200(JsonResult jsonResult){
		if(jsonResult==null) jsonResult = new JsonResult();
		jsonResult.setCode(ResultCodeUtil.ERROR_200);
		jsonResult.setMessage(ResultCodeUtil.ERROR_200_MSG);
		return jsonResult;
	}
	
	public JsonResult success(JsonResult jsonResult){
		if(jsonResult==null) jsonResult = new JsonResult();
		jsonResult.setCode(ResultCodeUtil.SUCCESS_100);
		jsonResult.setMessage(ResultCodeUtil.SUCCESS_100_MSG);
		return jsonResult;
	}
	
	public JsonResult error700(JsonResult jsonResult){
		if(jsonResult==null) jsonResult = new JsonResult();
		jsonResult.setCode(ResultCodeUtil.PARAMETER_NOT_VALID_700);
		jsonResult.setMessage(ResultCodeUtil.PARAMETER_NOT_VALID_700_MSG);
		return jsonResult;
	}
	
	public JsonResult error600(JsonResult jsonResult,String msg){
		if(jsonResult==null) jsonResult = new JsonResult();
		jsonResult.setCode(ResultCodeUtil.CUSTOM_ERROR_600);
		jsonResult.setMessage(msg);
		return jsonResult;
	}
}
