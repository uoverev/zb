package com.ypkj.zubu.util;

import java.util.HashMap;
import java.util.Map;

import com.ypkj.zubu.entity.Page;


public class ResultUtil {

	/**
	 * 获取固定格式的返回结果
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	public static PageData getResult(Integer code ,String msg,Object data){
		PageData pageData=new PageData();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		pageData.put("datalist", map);
		pageData.put("code", code);
		pageData.put("msg",msg);
		return pageData;
		
	}

	public static PageData getResultD(Integer code ,String msg,Object data){
		PageData pageData=new PageData();
		
		pageData.put("datalist", data);
		pageData.put("code", code);
		pageData.put("msg",msg);
		return pageData;
		
	}
	
	/**
	 * 获取固定格式的返回结果
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	public static PageData getResultPage(Integer code ,String msg,Object data,Map page){
		PageData pageData=new PageData();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		if(page!=null){
			map.put("page", page);
		}
		pageData.put("datalist", map);
		pageData.put("code", code);
		pageData.put("msg",msg);
		return pageData;
		
	}
	/**
	 * 组装推送参数map
	 * @param taskNum
	 * @param uid
	 * @param grab_uid
	 * @return
	 */
	public static Map<String,String> getExtras(String messageType,String taskNum,String uid,String grab_uid){
		 Map<String,String> map=new HashMap<String,String>();
		 if(messageType!=null && !"".equals(messageType)){
			 map.put("messageType", messageType);
		 }
		 if(taskNum!=null && !"".equals(taskNum)){
			 map.put("taskNum", taskNum);
		 }
		 if(uid!=null && !"".equals(uid)){
			 map.put("uid", uid);
		 }
		 if(grab_uid!=null && !"".equals(grab_uid)){
			 map.put("guid", grab_uid);
		 }
		return map;		
	}
	/**
	 * 判断接口传入参数是否合法
	 * @param par
	 * @param pd
	 * @return
	 */
	public static boolean isPageDate(String par[],Map map){
		for (String string : par) {
			if(map.containsKey(string)){
				Object str=map.get(string);
				if(str==null || "".equals(str.toString())){
					return false;
				}
			}else{
				return false;
			}
		}
		return true;
		
	}
	//获得起始记录 和结束记录
	public static Object page(int totalResult,int currentPage){
		Page page=new Page();
		Map<String, Object> map2=new HashMap<String, Object>();
		page.setCurrentPage(currentPage);//当前页
		page.setTotalResult(totalResult);//总记录数
		int pagecount=page.getTotalPage();//总页数
		int pagesize=page.getShowCount();//每页显示记录
		//起始记录
		int pagestart=page.getCurrentResult();//每页起始记录
		map2.put("pagecount", pagecount);
		map2.put("pagesize", pagesize);
		map2.put("pagestart", pagestart);
		return map2;
	}
}
