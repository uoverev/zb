package com.ypkj.zubu.service.newbie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.ResultUtil;
@Service("newbieService")
public class NewbieService {
	@Resource(name="daoSupport")
	private DaoSupport daosupport;
	
	/**
	 * 新手指引接口
	 * */
	public  Object  newbilezylist() throws Exception{
		ArrayList<Map<String, Object>> newp=null;
		try{
			Map<String, Object> result=new HashMap<String, Object>();
			newp=(ArrayList<Map<String, Object>>)daosupport.findForList("NewpeopleMapper.newp",null);
			
		}catch (Exception e){
			return ResultUtil.getResult(100, "服务器异常 ", newp);
		}	
		return ResultUtil.getResult(100, "成功 ", newp);
	}
}
