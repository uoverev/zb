package com.ypkj.zubu.service.system.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.controller.app.FileService;
import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.dao.TaskDao;
import com.ypkj.zubu.util.EncodeQRCODE;
import com.ypkj.zubu.util.ObjectUtil;
import com.ypkj.zubu.util.PageData;
import com.ypkj.zubu.util.ResultCodeUtil;
import com.ypkj.zubu.util.ResultUtil;

@Service("userQrCodeService")
public class UserQrCodeService
{
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name = "taskDao")
	private TaskDao taskDao;

	public Object updateGetQrCode(PageData pd, HttpServletRequest request) throws Exception
	{
		Map<String, Object> map2 = ObjectUtil.stringToMap(pd.get("DATA").toString());
		String[] par = new String[]{ "user_id"};
		if (!ResultUtil.isPageDate(par, map2))
		{
			return ResultUtil.getResult(ResultCodeUtil.PARAMETER_NOT_VALID_700, "参数不合法", par);
		}
		
		String userId=map2.get("user_id").toString();

		Map map=taskDao.findtUserByExmaple(userId);
		Object qrcode=map.get("qrcode");
		if(qrcode!=null && !"".equals(qrcode.toString())){
			return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "二维码获取成功", qrcode);
		}
		String imagePath=request.getSession().getServletContext().getRealPath("/")+"zubu"+userId+".gif";
		
		String logoPath=request.getSession().getServletContext().getRealPath("/")+"register/images/zubu.png";
		System.out.println(imagePath);
		//生成二维码
		boolean boo=EncodeQRCODE.Encode_QR_CODE(userId,imagePath,logoPath);
		String path="";
		if(boo){
			//上传到图片服务器
			path=FileService.upLoadQrCodeFile(userId,imagePath);
			if(path.length()>0){
				Map<String, String> userMap=new HashMap<String, String>();
				userMap.put("userId", userId);
				userMap.put("qrcode", path);
				Integer num = (Integer) dao.update("UserOperateMapper.modify", userMap);
				return ResultUtil.getResult(ResultCodeUtil.SUCCESS_100, "二维码获取成功", path);
			}
		}
		
		return ResultUtil.getResult(ResultCodeUtil.CUSTOM_ERROR_600, "二维码获取失败", "");

	}
}
