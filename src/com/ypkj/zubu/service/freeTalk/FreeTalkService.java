package com.ypkj.zubu.service.freeTalk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;

@Service("freeTalkService")
public class FreeTalkService
{
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public Object saveFreeTalk(PageData pd) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]
		{ "user_id", "startTime", "endTime" };
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		String startTime=map.get("startTime").toString();
		String endTime=map.get("endTime").toString();
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		if(sdf.parse(startTime).getTime()>sdf.parse(endTime).getTime()){
			map.put("startTime", sdf.parse(startTime));
			map.put("endTime", sdf.parse("24:00"));
			list.add(map);
			Map<String, Object> map2=new HashMap<String, Object>();
			map2.put("user_id", map.get("user_id"));
			map2.put("startTime", sdf.parse("00:00"));
			map2.put("endTime", sdf.parse(endTime));
			list.add(map2);
		}else{
			list.add(map);
		}
		
		Integer num=(Integer) dao.delete("FreeTalkMapper.delete", map.get("user_id"));
		num=(Integer) dao.save("FreeTalkMapper.save", list);
		
		if (num > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "设置成功", map);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.ERROR_200, "设置失败", map);
		}
	}
	
	public Object findFreeTalk(PageData pd) throws Exception
	{
		Map<String, Object> map = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]
		{ "user_id",};
		if (!ResultUtil.isPageDate(par, map))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		String startTime=map.get("startTime").toString();
		String endTime=map.get("endTime").toString();
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		if(sdf.parse(startTime).getTime()>sdf.parse(endTime).getTime()){
			map.put("startTime", sdf.parse(startTime));
			map.put("endTime", sdf.parse("24:00"));
			list.add(map);
			Map<String, Object> map2=new HashMap<String, Object>();
			map2.put("user_id", map.get("user_id"));
			map2.put("startTime", sdf.parse("00:00"));
			map2.put("endTime", sdf.parse(endTime));
			list.add(map2);
		}else{
			list.add(map);
		}
		
		Integer num=(Integer) dao.delete("FreeTalkMapper.delete", map.get("user_id"));
		num=(Integer) dao.save("FreeTalkMapper.save", list);
		
		if (num > 0)
		{
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "设置成功", map);
		} else
		{
			return ResultUtil.getResult(ResultCodeUtil.ERROR_200, "设置失败", map);
		}
	}

}
