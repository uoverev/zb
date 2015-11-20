package com.ypkj.zubu.service.dynamic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.crypto.Data;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;














import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.support.logging.Log;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.ypkj.zubu.controller.app.FileService;
import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.entity.Page;
import com.ypkj.zubu.util.LatLonUtil;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultUtil;
@Service("mydynamicservice")
@SuppressWarnings("all")
public class MydynamicService {
	 Map<String, Object> pagemap=new HashMap<String, Object>();
	 ArrayList<Map<String, Object>> pas=new ArrayList<Map<String,Object>>();
 	Map<String, Object> pa=new HashMap<String, Object>();	
	/**100004
	 * 我的动态接口
	 * 返回参数 : 3.发布的心情 4.转发的心情
	 * */
	@Resource(name="daoSupport")
	private DaoSupport daosupport;
	
	public Object Mydtlist(PageData pd){
		Page page =  new Page();
		Map<String, Object> result=new HashMap<String, Object>();
		ArrayList<Map<String, Object>> user_dynamic=new ArrayList<Map<String,Object>>();
		Map<String, Object>pagemap=null;
		//Map<String, Object> resultlist=new HashMap<String, Object>();
		try{
			//json字符串转为MAP对象
			 Map<String,Object> map = ObjectUtil.stringToMap(pd.getString("DATA"));
			 int mydtnum=(Integer)daosupport.findForObject("MydynamicMapper.userdynamicnum", map);
			 pagemap=(Map<String, Object>)ResultUtil.page(mydtnum, Integer.parseInt(map.get("currentPage").toString()));
			 map.put("pageu", pagemap);
		
			 user_dynamic=(ArrayList<Map<String,Object>>)daosupport.findForList( "MydynamicMapper.userdynamic", map);
			 for(int i=0;i<user_dynamic.size();i++){
				 user_dynamic.get(i).put("pricture", daosupport.findForList("MydynamicMapper.pricturelist",user_dynamic.get(i).get("dt_id")));
			 }
			 user_dynamic=(ArrayList<Map<String,Object>>)com_turn_praise_profession(user_dynamic, map.get("userid")+"");
		 	 pa.put("page",pagemap);
		 	 pa.put("data", user_dynamic);
		}catch (Exception e){
			e.printStackTrace();
			return ResultUtil.getResultD(-200, "服务器异常",null);
		}
		return  ResultUtil.getResultD(100, "查看成功",pa);
	}
	
	/**100007
	 * 动态查询接口
	 * 全部动态:1关注人的发布的任务和心情。2.附近人发布的任务和心情
	 * 与我相关:1别人给我定向推送的任务 2.别人转发，点赞我的任务3.别人转发，点赞我的心情
	 * */
	public  Object 	dynamiclist(PageData pd){
		 
		ArrayList<Map<String, Object>> alldynamic=new ArrayList<Map<String, Object>>();
	 	Map<String, Object> latlnt=new HashMap<String, Object>();
		//任务主题
	 	Map<String, Object> theme=new HashMap<String, Object>();
	 	Map<String, Object> task_id=new HashMap<String, Object>();
	 	//json字符串转为MAP对象
	 	double []  coordinate_minmax=new double[4];
	 	try{
			 	Map map = ObjectUtil.stringToMap(pd.getString("DATA"));
			 	Map<String, Object> lat_lnt=new HashMap<String, Object>();
			 	LatLonUtil latlonutil=new LatLonUtil();
			 	//附近人发布的任务/心情 
			 	switch(Integer.parseInt(map.get("types").toString())){
			 	case 1:			 		
			 		//String [] str={"userid","latitude","longitude","types","screen","currentPage","sex"};
					//获得1用户周围的人的坐标范围
				 	double latitude=(Double.parseDouble(map.get("latitude")+""));
				 	double longitude=(Double.parseDouble(map.get("longitude")+""));
				 	coordinate_minmax=latlonutil.getAround(latitude,longitude, 1000000);
			 		map.put("minlat", coordinate_minmax[0]);
		 			map.put("minlnt", coordinate_minmax[1]);
		 			map.put("maxlat", coordinate_minmax[2]);
		 			map.put("maxlnt", coordinate_minmax[3]);
//			 		Boolean  parameter=ResultUtil.isPageDate(str, map);
//				 	if(parameter==false)
//					{
//						return  ResultUtil.getResultD(-300, "参数不合法", str);
//					}	
			 		if(map.get("screen").toString().equals("1"))
				 		{
				 			
				 			map.put("type", 1);
				 			int alldynamicount=(Integer)daosupport.findForObject("MydynamicMapper.alldynamicpagecount", map);
						 	pagemap=(Map<String, Object>)ResultUtil.page(alldynamicount, Integer.parseInt(map.get("currentPage").toString()));
				 			map.put("pageu", pagemap);
				 			alldynamic=(ArrayList<Map<String, Object>>)daosupport.findForList("MydynamicMapper.alldynamic", map);		 		
				 			for(int i=0;i<alldynamic.size();i++){
				 				if(alldynamic.get(i).get("task_id")!=null&&!alldynamic.get(i).get("task_id").toString().equals("0")){
				 			//任务抢单人
				 					//alldynamic.get(i).put("single", daosupport.findForList("MydynamicMapper.findRunnersLimit",alldynamic.get(i)));
				 					task_id.put("task_id", alldynamic.get(i).get("task_id"));
				 			//任务类别
				 					theme=(Map<String, Object>)daosupport.findForObject("MydynamicMapper.task", task_id);
				 					alldynamic.get(i).put("theme",theme.get("theme") );
				 				
				 				}
				 				if(alldynamic.get(i).get("dt_id")!=null&&!alldynamic.get(i).get("dt_id").toString().equals("0")){
				 					alldynamic.get(i).put("pricture", daosupport.findForList("MydynamicMapper.pricturelist",alldynamic.get(i).get("dt_id")));
				 				}
				 				
				 			}
				 		//关注人发布的任务/心情，转发的任务/心情
				 		}else if(map.get("screen").toString().equals("0")){			 			
				 			map.put("type", 0);
				 			int alldynamicount=(Integer)daosupport.findForObject("MydynamicMapper.alldynamicpagecount", map);
						 	pagemap=(Map<String, Object>)ResultUtil.page(alldynamicount, Integer.parseInt(map.get("currentPage").toString()));
				 			map.put("pageu", pagemap);
				 			
				 			alldynamic=( ArrayList<Map<String, Object>>)daosupport.findForList("MydynamicMapper.alldynamic", map);		 		
				 			for(int i=0;i<alldynamic.size();i++){
				 					if(alldynamic.get(i).get("dt_id")!=null&&!alldynamic.get(i).get("dt_id").toString().equals("0")){
					 					alldynamic.get(i).put("pricture", daosupport.findForList("MydynamicMapper.pricturelist",alldynamic.get(i).get("dt_id")));
		
				 					}	
				 					if(alldynamic.get(i).get("task_id")!=null&&!alldynamic.get(i).get("task_id").toString().equals("0")){
//				 						alldynamic.get(i).put("single", daosupport.findForList("MydynamicMapper.findRunnersLimit",alldynamic.get(i)));
//					 					
					 					task_id.put("task_id", alldynamic.get(i).get("task_id"));
					 					theme=(Map<String, Object>)daosupport.findForObject("MydynamicMapper.task", task_id);
					 					alldynamic.get(i).put("theme",theme.get("theme") );
				 					}				 				
				 			}
				 		
				 		}
			 		
			 		else if(map.get("screen").toString().equals("3")){
			 			//全部动态
			 			int alldynamicount=(Integer)daosupport.findForObject("MydynamicMapper.alldynamicpagecount", map);
					 	pagemap=(Map<String, Object>)ResultUtil.page(alldynamicount, Integer.parseInt(map.get("currentPage").toString()));
//			 			if(pagemap.get(""))
					 	map.put("pageu", pagemap);
					 	
			 			alldynamic=(ArrayList<Map<String, Object>>)daosupport.findForList("MydynamicMapper.alldynamic", map);				 		
				 			for(int i=0;i<alldynamic.size();i++){
				 				if(alldynamic.get(i).get("task_id")!=null&&!alldynamic.get(i).get("task_id").toString().equals("0")){
//				 					alldynamic.get(i).put("single", daosupport.findForList("MydynamicMapper.findRunnersLimit",alldynamic.get(i)));
//				 					
				 					task_id.put("task_id", alldynamic.get(i).get("task_id"));
				 					theme=(Map<String, Object>)daosupport.findForObject("MydynamicMapper.task", task_id);
				 					alldynamic.get(i).put("theme",theme.get("theme") );
				 					
				 				}
				 				if(alldynamic.get(i).get("dt_id")!=null&&!alldynamic.get(i).get("dt_id").toString().equals("0")){
				 					alldynamic.get(i).put("pricture", daosupport.findForList("MydynamicMapper.pricturelist",alldynamic.get(i).get("dt_id")));
				 				}
				 			}
			 		}
			 		alldynamic=(ArrayList<Map<String,Object>>)com_turn_praise_profession(alldynamic, map.get("userid")+"");
			 			
				 		pa.put("page",pagemap);
				 		pa.put("data", alldynamic);
				 		return  ResultUtil.getResultD(100, "成功",pa);	
			 		//与我相关
			 		case 0:		
			 			
			 			int pagecount=(Integer)daosupport.findForObject("MydynamicMapper.Mycorrelationnum", map);
			 			pagemap=(Map<String, Object>)ResultUtil.page(pagecount,Integer.parseInt(map.get("currentPage").toString()));
			 			map.put("pageu", pagemap);
			 			pas.add(pagemap);
			 			alldynamic=(ArrayList<Map<String, Object>>)daosupport.findForList("MydynamicMapper.Mycorrelation", map);
			 			for(int i=0;i<alldynamic.size();i++){
//			 				if(alldynamic.get(i).get("task_id")!=null&&!alldynamic.get(i).get("task_id").toString().equals("0")){
//			 					alldynamic.get(i).put("single", daosupport.findForList("MydynamicMapper.findRunnersLimit",alldynamic.get(i)));
//			 				}
			 				if(alldynamic.get(i).get("dt_id")!=null&&!alldynamic.get(i).get("dt_id").toString().equals("0")){
			 					alldynamic.get(i).put("pricture", daosupport.findForList("MydynamicMapper.pricturelist",alldynamic.get(i).get("dt_id")));
			 				}
			 			
			 			}
			 			
				 		pa.put("page",pagemap);
				 		pa.put("data", alldynamic);
				 		return  ResultUtil.getResultD(100, "成功",pa);	
			 	}
			 	}catch (Exception e){
			 e.printStackTrace();
			 ResultUtil.getResult(-200, "查询失败", null);
		 }
		 	return null;
	}
	/**
	 * 转发接口
	 * 200014
	 * */
	public Object savezf(PageData pd){
		Map<String, Object> map=null;
		Map<String, Object> result=new HashMap<String, Object>();
		String [] str ={"userid","tid","types","fx_content"};
		try{
		map=ObjectUtil.stringToMap(pd.getString("DATA"));
		boolean isp=ResultUtil.isPageDate(str, map);
		if(isp!=false){
				daosupport.save("MydynamicMapper.addzf", map);
			 
			}else{
				return ResultUtil.getResultD(-600, "参数不合法", map);
			}
		
		}catch (Exception e){
			 return ResultUtil.getResultD(-200, "服务器异常", null);
		}
		  return ResultUtil.getResult(100, "成功", null);
	}
	/**
	 * 200003)
	 * 动态发布接口
	 * */
	public Object save_dt(PageData pd,MultipartHttpServletRequest  Filereq){
 		Map<String, Object> map=null;
		Map<String, Object> result=new HashMap<String, Object>();
		int count=0;		
		
//		 MultipartFile file = Filereq.getFile("imageFile");
		Iterator<String> iter = Filereq.getFileNames();
	try{
		map=ObjectUtil.stringToMap(pd.getString("DATA"));	
		MultiValueMap<String, MultipartFile> filemap = Filereq.getMultiFileMap();
		List<String> keys=new ArrayList<String>(); 
		for (String key : filemap.keySet()) {
			MultipartFile file = filemap.get(key).get(0);	
				if (file != null && file.getSize()>0) {
					count += 1;
				}else{
					keys.add(key);
				}
		}
		//去处空图片
		for (String key : keys)
		{
			filemap.remove(key);
		}
		int add_dt=(Integer)daosupport.save("MydynamicMapper.add_dt", map);
		if(add_dt>0){
			if(count>0){			
			
				//调用文件上传服务	
				String url=FileService.upLoadFile(Filereq);
				String[] picUrl = url.split(",");
				if(picUrl.length>0&&picUrl!=null){
					for(String picture:picUrl){
						if(!picture.equals("")){
							map.put("url", picture);
							daosupport.save("MydynamicMapper.add_dt_picture", map);
						}
					}	
				}
			}
		}
			else{
				return ResultUtil.getResultD(-600, "动态添加失败", null);
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
		return ResultUtil.getResult(100, "成功", null);
	}
	
	/**
	 * 200041
	 * 删除动态
	 */
/*	public  Object  delete_dtRow(PageData pd){
		
	}*/
//	public Object gztask_gzmood(Map<String,Object> map) {
//		   ArrayList<Map<String, Object>> mm=new ArrayList<Map<String,Object>>();  	 
//		   try {
//			
//		
//		   //别人给我定向推送的任务
//		   ArrayList<Map<String, Object>>	bdxts=(ArrayList<Map<String, Object>>)daosupport.findForList("MydynamicMapper.bdxts", map);
//		   ArrayList<Map<String, Object>>	brdzrw=null;	   				   	 
//		   //别人转发 ，点赞我的任务 
//		   brdzrw=(ArrayList<Map<String, Object>>)daosupport.findForList("MydynamicMapper.brdzrw", map); 
//		   //别人 转发 ，点赞我的心情
//		   ArrayList<Map<String, Object>>	brdzxq=(ArrayList<Map<String, Object>>)daosupport.findForList("MydynamicMapper.brdzxq", map);
//		   mm.addAll(bdxts);
//		   mm.addAll(brdzrw);
//		   mm.addAll(brdzxq);
//		   return sort(mm);
//		   } catch (Exception e) {
//				e.printStackTrace();
//				return null;
//			}
//	}
	//排序顺序
//	public  Object sort(ArrayList<Map<String, Object>> mm) throws Exception{
//		ArrayList<Map<String, Object>> ms=new ArrayList<Map<String,Object>>();
//		for(int i=0;i<mm.size();i++){
//			for(int j=i+1;j<mm.size()-1;j++){
//				Map<String, Object>	 a1=mm.get(i);
//				if(((Date)mm.get(i).get("create_time")).before((Date)mm.get(j).get("create_time"))){
//					Map<String, Object>	 temp =new HashMap<String, Object>();	
//							temp=(Map<String, Object>)mm.get(j);
//							a1=temp;
//							temp=a1;
//							
//				}
//			}
//			if(!mm.get(i).get("task_id").toString().equals("0"))
//			{
//				mm.get(i).put("single", daosupport.findForList("TaskMapper.findRunnersLimit",mm.get(i).get("task_id")));
//			}
//			if(!mm.get(i).get("dt_id").toString().equals("0")){
//				mm.get(i).put("pricture", daosupport.findForList("MydynamicMapper.pricturelist",mm.get(i).get("dt_id")));
//			}
//			
//		}
//	 return mm;
//	}
	//获得评论数，转发数，是否点过赞，是否有职业认证
	public Object  com_turn_praise_profession(ArrayList<Map<String, Object>> alldynamic,String Myid){
		//评论数
		try {
				
			Map<String, Object> parameter_p=new HashMap<String, Object>();
			
			for(int i=0;i<alldynamic.size();i++){
				
				
				parameter_p.put("Myid",Myid );
				parameter_p.put("userid", alldynamic.get(i).get("userid") );
				parameter_p.put("bzfuserid", alldynamic.get(i).get("bzfuserid") );
				
				if(alldynamic.get(i).get("task_id")!=null&&!alldynamic.get(i).get("task_id").toString().equals("0")){
					//任务ID
					parameter_p.put("tid", alldynamic.get(i).get("task_id"));
					parameter_p.put("type", 0);			
 				}
 				if(alldynamic.get(i).get("dt_id")!=null&&!alldynamic.get(i).get("dt_id").toString().equals("0")){
 					parameter_p.put("tid", alldynamic.get(i).get("dt_id"));
					parameter_p.put("type", 1);		
 				}
 				Map<String, Object> p=(Map<String, Object>)daosupport.findForObject("MydynamicMapper.pslw", parameter_p);
 				alldynamic.get(i).put("comment_num", p.get("comment_num"));
 				alldynamic.get(i).put("transpond_num", p.get("transpond_num"));
 				alldynamic.get(i).put("isPraise", p.get("isPraise"));
 				alldynamic.get(i).put("professionState", p.get("professionState"));
 				alldynamic.get(i).put("bzfprofessionState", p.get("bzfprofessionState"));			
 	/*			alldynamic.get(i).put("uisvip", p.get("uisvip"));
 				alldynamic.get(i).put("bzfisvip", p.get("bzfisvip"));	*/		
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return alldynamic;
	}
/*	//上传文件
	public  Object  uploadfile(MultipartHttpServletRequest rquest){
		ArrayList<File> filemode=new ArrayList<File>();
		HttpClient client = new HttpClient();
		//请求地址
		MultipartPostMethod mPost = new MultipartPostMethod("http://120.55.125.76:8099/FileServer/album/save.do");
		//表示如果对方连接状态5分钟没有收到数据的话强制断开客户端
		mPost.getParams().setSoTimeout(500000);
		mPost.setRequestHeader("content-type", "multipart/form-data; boundary=,");
		
	}*/
	
}
