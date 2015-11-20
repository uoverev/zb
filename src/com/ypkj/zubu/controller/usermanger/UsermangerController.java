package com.ypkj.zubu.controller.usermanger;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ypkj.zubu.controller.base.BaseController;
import com.ypkj.zubu.entity.Page;
import com.ypkj.zubu.entity.system.Menu;
import com.ypkj.zubu.service.usermanger.UsermangerService;
import com.ypkj.zubu.util.AppUtil;
import com.ypkj.zubu.util.Const;
import com.ypkj.zubu.util.ObjectExcelView;
import com.ypkj.zubu.util.PageData;

/** 
 * 类名称：UsermangerController
 * 创建人：FH 
 * 创建时间：2015-06-17
 */
@Controller
@RequestMapping(value="/usermanger")
public class UsermangerController extends BaseController {
	
	@Resource(name="usermangerService")
	private UsermangerService usermangerService;
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, "新增Usermanger");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USERMANGER_ID", this.get32UUID());	//主键
		pd.put("REGTIME", new Date());
		usermangerService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out){
		logBefore(logger, "删除Usermanger");
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			usermangerService.delete(pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, "修改Usermanger");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		usermangerService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 列表
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page){
		logBefore(logger, "列表Usermanger");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			page.setPd(pd);
			List<PageData>	varList = usermangerService.list(page);	//列出Usermanger列表
			this.getHC(); //调用权限
			mv.setViewName("usermanger/usermanger/usermanger_list");
			mv.addObject("varList", varList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd(){
		logBefore(logger, "去新增Usermanger页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("usermanger/usermanger/usermanger_edit");
			mv.addObject("msg", "save");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	/**
	 * 去修改页面
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit(){
		logBefore(logger, "去修改Usermanger页面");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = usermangerService.findById(pd);	//根据ID读取
			mv.setViewName("usermanger/usermanger/usermanger_edit");
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}	
	
	/**
	 * 批量删除
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() {
		logBefore(logger, "批量删除Usermanger");
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				usermangerService.deleteAll(ArrayDATA_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/*
	 * 导出到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel(){
		logBefore(logger, "导出Usermanger到excel");
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			Map<String,Object> dataMap = new HashMap<String,Object>();
			List<String> titles = new ArrayList<String>();
			titles.add("用户ID");	//1
			titles.add("广告商ID");	//2
			titles.add("昵称");	//3
			titles.add("帐号");	//4
			titles.add("手机号");	//5
			titles.add("头像");	//6
			titles.add("余额");	//7
			titles.add("支付宝姓名");	//8
			titles.add("支付宝帐号");	//9
			titles.add("状态");	//10
			titles.add("注册时间");	//11
			titles.add("邀请人ID");	//12
			dataMap.put("titles", titles);
			List<PageData> varOList = usermangerService.listAll(pd);
			List<PageData> varList = new ArrayList<PageData>();
			for(int i=0;i<varOList.size();i++){
				PageData vpd = new PageData();
				vpd.put("var1", varOList.get(i).getString("USERID"));	//1
				vpd.put("var2", varOList.get(i).getString("ADVEID"));	//2
				vpd.put("var3", varOList.get(i).getString("NICKNAME"));	//3
				vpd.put("var4", varOList.get(i).getString("ACCOUNT"));	//4
				vpd.put("var5", varOList.get(i).get("PHONE").toString());	//5
				vpd.put("var6", varOList.get(i).getString("HEAD"));	//6
				vpd.put("var7", varOList.get(i).getString("BALANCE"));	//7
				vpd.put("var8", varOList.get(i).getString("ALIPAY_NAME"));	//8
				vpd.put("var9", varOList.get(i).getString("ALIPAY_ACCOUNT"));	//9
				vpd.put("var10", varOList.get(i).getString("STATE"));	//10
				vpd.put("var11", varOList.get(i).getString("REGTIME"));	//11
				vpd.put("var12", varOList.get(i).getString("INVITER"));	//12
				varList.add(vpd);
			}
			dataMap.put("varList", varList);
			ObjectExcelView erv = new ObjectExcelView();
			mv = new ModelAndView(erv,dataMap);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/* ===============================权限================================== */
	public void getHC(){
		ModelAndView mv = this.getModelAndView();
		HttpSession session = this.getRequest().getSession();
		Map<String, String> map = (Map<String, String>)session.getAttribute(Const.SESSION_QX);
		mv.addObject(Const.SESSION_QX,map);	//按钮权限
		List<Menu> menuList = (List)session.getAttribute(Const.SESSION_menuList);
		mv.addObject(Const.SESSION_menuList, menuList);//菜单权限
	}
	/* ===============================权限================================== */
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
