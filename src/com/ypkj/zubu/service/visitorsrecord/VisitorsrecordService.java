package com.ypkj.zubu.service.visitorsrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.entity.Page;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultUtil;
/**
 * 
 * @author 邓辉
 * 时间 :2015.8.23
 */
@Service("visitorsrecordService")
public class VisitorsrecordService {
	@Resource(name="daoSupport")
	private  DaoSupport daoSupport;
	
	/**200008
	 * 访客记录添加接口 
	 * */
//	public Object  saveVisitorsrecord(PageData pd){
//		ArrayList<Map<String, Object>> Visitorsrecord=null;
//		try{
//			Map Visitorsre=ObjectUtil.stringToMap(pd.getString("DATA"));
//			
////			访客中ID相同的用户 只修改访问时间
////			int count =(Integer)daoSupport.findForObject("visitorIMMapper.visitornum", Visitorsre);
////			if(count<0){
//				//添加访客到 访客记录表中
//				daoSupport.save("visitorIMMapper.addvsrecord", Visitorsre);
//				//Visitorsrecord=(ArrayList<Map<String, Object>>)daoSupport.findForList("visitorIMMapper.vistor", Visitorsre);
////			}else{
////				//修改访客时间
////				daoSupport.update("visitorIMMapper.Updatevistor", Visitorsre);
////			}			
//		}catch (Exception e){
//			e.printStackTrace();
//			return ResultUtil.getResultD(-200, "服务器异常",null );
//		}
//		return ResultUtil.getResultD(100, "成功", Visitorsrecord);
//	}
	/**
	 * 访客记录查询
	 * 100024
	 * */
	@SuppressWarnings("all")
	public  Object  Visitorsrecordlist(PageData pd){
		Map<String, Object> pagemap=null;
		ArrayList<Map<String, Object>> Visitorsrecord=null;
		Map<String, Object> pa=new HashMap<String, Object>();	
		try {
			
			Map Visitorsre=(Map)ObjectUtil.stringToMap(pd.getString("DATA"));
			int visitnum=(Integer)daoSupport.findForObject("visitorIMMapper.visitnum", Visitorsre);
			pagemap=(Map<String, Object>)ResultUtil.page(visitnum,(Integer.parseInt(Visitorsre.get("currentPage").toString())));
			Visitorsre.put("pageu", pagemap);
			Visitorsrecord=(ArrayList<Map<String, Object>>)daoSupport.findForList("visitorIMMapper.vistor", Visitorsre);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.getResultD(-200, "服务器异常", null);
		}
		pa.put("page",pagemap);
	    pa.put("data", Visitorsrecord);
		return ResultUtil.getResultD(100, "成功", pa);
	}
}
