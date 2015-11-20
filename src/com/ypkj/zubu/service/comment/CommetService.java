package com.ypkj.zubu.service.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultUtil;
/**
 * 类文件:评论添加接口
 * @author 邓辉
 * 创建 时间 ：2015.8.23
 * 200013
 */
@Service("commetService")
public class CommetService {
	@Resource(name="daoSupport")
	private DaoSupport daoSupport;
	
	public Object  savecomment(PageData pd){
		Map map=new HashMap();
		List<Map<String, Object>> result=null;
		
		try{
			map=ObjectUtil.stringToMap(pd.getString("DATA"));
			String [] str={"userid","content","ithingsid","Respond_eopleId","parent_id","ctype"};
			boolean isp=ResultUtil.isPageDate(str, map);
			if(isp==false){
				return ResultUtil.getResult(-300, "参数不对", str);	
			}
			daoSupport.save("commentMapper.adddtcomment",map);
			result=getcommentlist(map);
		}catch (Exception e){
			throw new RuntimeException();
		}
			return ResultUtil.getResult(100, "添加成功", result);
	}
	/**
	 * 评论查询接口 
	 * 100018
	 * */
	public  Object  commentlist(PageData pd){
			Map<String, Object> ut=new HashMap<String, Object>();
		ArrayList<Map<String, Object>> leave_msg=null;
		ArrayList<Map<String, Object>> respond_eopleId=null;
		//留言信息
		try{
			Map map=ObjectUtil.stringToMap(pd.getString("DATA"));
			leave_msg=(ArrayList<Map<String,Object>>)getcommentlist(map);
			Map<String, Object> node=null;
			for(int i=0;i<leave_msg.size();i++){
				if(leave_msg.get(i).get("node")==null){
					leave_msg.get(i).put("node", new ArrayList<Map<String, Object>>());	
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return	ResultUtil.getResult(-200, "服务器异常", null);
		}
	  return	ResultUtil.getResult(100, "成功", leave_msg);
	}
	
		//留言人回复人信息
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getcommentlist(Map map) throws Exception{
		List<Map<String, Object>> lymsg=
				(ArrayList<Map<String, Object>>)daoSupport.findForList("commentMapper.TMessage",map); 
		List<Map<String, Object>> ROOT=new ArrayList<Map<String,Object>>();
		for(Map<String, Object> root : lymsg){  	
			    boolean mark = false;
			    for(Map<String, Object> node2 : lymsg){  
			    	if(!(root.get("parent_id")==null&&("0").equals(root.get("parent_id"))) && root.get("parent_id").equals(node2.get("ly_id"))){  
			            mark = true;  
			            if(node2.get("node")==null)
			            	node2.put("node", new ArrayList<Map<String,Object>>() );
			            
			            ((List) node2.get("node")).add(root);
			            break; 
			        }  
			   	}  
			    	if(!mark){  
			    	
			    		ROOT.add(root);   
			    }  			    	
	}
		return ROOT;
}
}