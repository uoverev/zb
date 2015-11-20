package com.ypkj.zubu.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

@Repository("taskDao")
public class TaskDao
{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 查询用户信息
	 * @param map2
	 * @return
	 * @throws Exception
	 */
	public Map findtUserByExmaple(String userId) throws Exception{
		Map map2=new HashMap();
		map2.put("userId", userId);
		Map mapUser = (Map) dao.findForObject(
				"UserLoginMapper.findtUserByExmaple",map2);
		return mapUser;
	}
	

}
