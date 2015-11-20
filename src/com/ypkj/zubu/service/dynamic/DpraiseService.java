package com.ypkj.zubu.service.dynamic;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;

/**
 * 
 * @author 邓辉
 * 时间:2015.8.23
 */
@Service("dpraiseService")
public class DpraiseService {
	@Resource(name="daoSupport")
	private DaoSupport daoSupport;
	/**200015
	 * 点赞
	 * 0 点赞  1取消点赞
	 * */
	public  Object   updateDp(PageData pd){
		String [] str={"isdz","types","dt_id","userid","task_id"};
		Map DZ=new HashMap();
		try{
		Map<String, Object> map=ObjectUtil.stringToMap(pd.getString("DATA"));
		Boolean  parameter=ResultUtil.isPageDate(str, map);
		if(parameter==false)
		{
			return  ResultUtil.getResultD(ResultCodeUtil.CUSTOM_ERROR_600, "参数不合适", null);
		}
		DZ=	ObjectUtil.stringToMap(pd.getString("DATA"));
		int count=(Integer)daoSupport.findForObject("dpraiseMapper.dzcx", DZ);
		if(DZ.get("isdz").toString().equals("0"))//点赞
		{
			if(count>0){
				//不能点赞
				Map<String,Object> state=new HashMap<String, Object>();
				state.put("state", 0);
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "亲，你已点过赞了 ^_^!!", state);
			}else{
				//给心情点赞
				if(DZ.get("dt_id")!=null&&!DZ.get("dt_id").toString().equals("0")){
					daoSupport.update("dpraiseMapper.DZd",DZ);
				}if(DZ.get("task_id")!=null&&!DZ.get("task_id").toString().equals("0")){
					daoSupport.update("dpraiseMapper.DZt",DZ);
				}
				int add=(Integer)daoSupport.save("dpraiseMapper.addtj", DZ);
				if(add>0){
					return ResultUtil.getResult(100, "点赞成功", null);
				}
				else {
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "点赞失败", null);
				}
			}
		}else if(DZ.get("isdz").toString().equals("1")){//取消点赞
			int issuccess=0;
			if(DZ.get("dt_id")!=null&&!DZ.get("dt_id").toString().equals("0")){
				//动态表中的点赞-1
				issuccess=(Integer)daoSupport.update("dpraiseMapper.DZqd",DZ);
			}if(DZ.get("task_id")!=null&&!DZ.get("task_id").toString().equals("0")){
				//任务表中的动态-1
				issuccess=(Integer)daoSupport.update("dpraiseMapper.DZqt",DZ);
			}
			//删除点赞记录表中的记录
			if(issuccess>0){
				int del=(Integer)daoSupport.delete("dpraiseMapper.del", DZ);
				if(del>0){
					return ResultUtil.getResult(100, "取消点赞成功", null);
				}else{
					return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "取消点赞失败", null);
				}
			}else{
				return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "取消点赞失败", null);
			}
		}
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
		return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "失败", null);
	}
}
