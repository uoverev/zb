package com.ypkj.zubu.service.userfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.filefilter.MagicNumberFileFilter;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.entity.Page;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultUtil;

@Service("appkuCService")
public class AppkuCService {
	@Resource(name = "daoSupport")
	private DaoSupport daosupport;

	/**
	 * 粉丝查询接口 用户ID 查询该用户下的所有粉丝信息
	 * */

	public Object fslist(PageData pd) throws Exception {
		 Map<String, Object> pagemap=new HashMap<String, Object>();
		Map<String, Object> pa=new HashMap<String, Object>();	
		Page page =  new Page();
		ObjectMapper obm = new ObjectMapper();
		Map<String, Object> result = new HashMap<String, Object>();
		// 把JSON 字符串转成 MAP集合
		Map fsid = obm.readValue(pd.get("DATA").toString(), Map.class);
		ArrayList<Map<String, Object>> fslist=null;
		try {
			 int fsnum=(Integer)daosupport.findForObject("AppMapper.fsnum", fsid);
			 pagemap=(Map<String, Object>)ResultUtil.page(fsnum, Integer.parseInt(fsid.get("currentPage").toString()));
			 fsid.put("pageu", pagemap);
			 fslist = (ArrayList<Map<String, Object>>) daosupport
						.findForList("AppMapper.fs", fsid);
		} catch (Exception e) {
			e.printStackTrace();
			return	ResultUtil.getResult(-200, "服务器异常", fslist);
		}
		 pa.put("page",pagemap);
	 	 pa.put("data", fslist);
		return	ResultUtil.getResultD(100, "查看成功", pa);
	};
}
