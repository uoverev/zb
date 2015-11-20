package com.ypkj.zubu.service.systemMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultUtil;
/**
 * 
 * @author 邓辉
 * 时间:2015.8.24
 * 100032
 */
@SuppressWarnings("all")
@Service("systemSerivce")
public class SystemSerivce {
	@Resource(name="daoSupport")
	private DaoSupport daoSupport;
	//系统推送消息查询
	public  Object  systemtsmsg(PageData pd){	
		ArrayList<Map<String, Object>> rsult=null;
		Map<String, Object> map=new HashMap<String, Object>();
		Map<String, Object> pages=new HashMap<String, Object>();
		Map<String, Object> page=new HashMap<String, Object>();
		
		try{
		map=ObjectUtil.stringToMap(pd.getString("DATA"));
		int totalResult=(Integer)daoSupport.findForObject("SystemMapper.msgnum", null);
		pages=(Map<String, Object>)ResultUtil.page(totalResult, Integer.parseInt(map.get("currentPage")+""));
		map.put("pagestart", pages.get("pagestart"));
		map.put("pagesize",pages.get("pagesize"));
		rsult= (ArrayList<Map<String, Object>>)daoSupport.findForList("SystemMapper.systemts", map);	
		page.put("page",pages );
		page.put("data", rsult);
		}catch (Exception e){
			e.printStackTrace();
			return ResultUtil.getResultD(-200, "服务器异常", null);
		}
		return ResultUtil.getResultD(100, "成功 ", page);
	}
}
