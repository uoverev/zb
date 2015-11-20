package com.ypkj.zubu.service.mbrprice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultUtil;

@Service("hypriceService")
@SuppressWarnings("all")
public class HypricesService {
	@Resource(name="daoSupport")
	private DaoSupport daosupport;
	/**
	 * 100011
	 * 会员价格查询接口    0开通  1未开通 2已过期
	 * **/
	public Object findhHpricelist(PageData pd){
		ArrayList<Map<String, Object>> Hyprice=new ArrayList<Map<String,Object>>();
		Map<String, Object> state=new HashMap<String, Object>();
		try{
			
			Map<String, Object> map=(Map<String, Object>)ObjectUtil.stringToMap(pd.getString("DATA"));
			Hyprice=(ArrayList<Map<String, Object>>)daosupport.findForList("HypricesMapper.Hyprice", null);
			int ishy=(Integer)daosupport.findForObject("HypricesMapper.t1", map);
			if(ishy>0){
				state.put("state", 0);
				int pastdue=(Integer)daosupport.findForObject("HypricesMapper.t2", map);
				if(pastdue>0){
					state.put("state", 2);
				}else{
					state.put("state", 0);
				}
			}
			else{
				state.put("state", 1);
			}
			Hyprice.add(state);
		}catch (Exception e){
			
			e.printStackTrace();
			return ResultUtil.getResultD(-200,"服务器异常" , null);
		}
		return ResultUtil.getResultD(100,"成功" , Hyprice);
	}
	
	/**
	 * 200022
	 * 会员开通接口
	 * */
	public Object saveMemberdredge(PageData pd){
		
		Map<String, Object> result=new HashMap<String, Object>();
		String s=null;
		try{
			Map<String, Object> map=(Map<String, Object>)ObjectUtil.stringToMap(pd.getString("DATA"));
			int ishy=(Integer)daosupport.findForObject("HypricesMapper.t1", map);
			int periodValidity=0;
			String [] str={"userid","yj","zk","jg","vId","dateType","pay_type"};
			
			boolean  parameter=ResultUtil.isPageDate(str, map);
			if(parameter==false)
			{
				return ResultUtil.getResultD(-300, "参数不正确", str);
			}			
			if(map.get("pay_type").toString().equals("0")){	//	平台支付
				//用户用自己的余额减去开通会员的钱
				int usermoneys=(Integer)daosupport.update("HypricesMapper.updatemoneys", map);
				if(usermoneys<1){
					return ResultUtil.getResultD(-400, "余额不足", null);
				}
			}
			//年
			if(map.get("dateType").toString().equals("0")){
				periodValidity=365;
			}
			//月
			if(map.get("dateType").toString().equals("1")){
				periodValidity=30;
			}
			//季度
			if(map.get("dateType").toString().equals("2")){
				periodValidity=90;
			}
			map.put("periodValidity", periodValidity);
			//是会员
			if(ishy>0){		
				int gq=(Integer)daosupport.findForObject("HypricesMapper.t2", map);
				// 有效时间内
				if(gq>0){
					int alter=(Integer)daosupport.update("HypricesMapper.updatehytime2", map);
				}
				else{
					
					int alter=(Integer)daosupport.update("HypricesMapper.updatehytime", map);
				}
				int add =(Integer)daosupport.save("HypricesMapper.Memberdredge", map);
			}
			else{
				daosupport.save("HypricesMapper.Memberdredge", map);
				daosupport.save("HypricesMapper.addMember", map);
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
			return ResultUtil.getResultD(100, "成功", null);
	}
}
