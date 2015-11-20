package com.ypkj.zubu.service.dynamic;

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
 * 时间 :2015.8.24
 *
 */
@Service("shareService")
public class ShareService {
	@Resource(name="daoSupport")
	private DaoSupport daosupport;
	/**
	 * 分享接口 
	 */
	public Object  saveshare(PageData pd){
		Map map=new HashMap();
		try{
		map=ObjectUtil.stringToMap(pd.getString("DATA"));
		//添加分享记录
		daosupport.save("ShareMapper.addfx",map);
		}catch (Exception e){
			return ResultUtil.getResult(-200, "服务器异常", null);
		}
		return ResultUtil.getResult(100, "分享成功", null);
		}
}

