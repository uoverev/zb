package com.ypkj.zubu.controller.auction;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ypkj.zubu.service.auction.bean.AuctionList;
import com.ypkj.zubu.service.auction.exception.AlreadyHighException;
import com.ypkj.zubu.service.auction.exception.NoMarginException;
import com.ypkj.zubu.service.auction.exception.SelfBidNotAllowedException;
import com.ypkj.zubu.service.auction.service.AuctionService;
import com.ypkj.zubu.util.ResultUtil;
import com.ypkj.zubu.web.common.AbstractController;
import com.ypkj.zubu.web.common.JsonResult;

/**
 * 
 * @ClassName: AuctionController
 * @Description: TODO
 * @author: tangqiang
 * @date: 2015年11月19日 下午4:41:45
 */
@Controller
@RequestMapping("auc")
public class AuctionController extends AbstractController{
	
	@Autowired
	AuctionService auctionService;

	/**
	 * 申请拍卖
	 * @param request
	 * @param auctionApply
	 * @return
	 */
	@RequestMapping("apply")
	@ResponseBody
	public JsonResult apply(HttpServletRequest request , @RequestParam Map<String, Object> auctionApply){
		JsonResult json = new JsonResult();
		String[] par = new String[]
		{ "name","company","position","phone","userId" };
		if (!ResultUtil.isPageDate(par, auctionApply)){
			return error700(json);
		}
		try {
			auctionService.apply(auctionApply);
			return success(json);
		} catch (Exception e) {
			e.printStackTrace();
			return error200(json);
		}
	}
	
	/**
	 * 拍卖列表
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public JsonResult list(HttpServletRequest request,@RequestParam Map<String, Object> map){
		JsonResult json = new JsonResult();
		String[] par = new String[]
		{ "pageNo","pageSize","userId"};
		if (!ResultUtil.isPageDate(par, map)){
			return error700(json);
		}
		try {
			List<AuctionList> list = auctionService.findAuctionList(map);
			json.appendData("aucList", list);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return error200(json);
		}
	}
	
	@RequestMapping("bid")
	@ResponseBody
	public JsonResult bid(HttpServletRequest request,@RequestParam Map<String, Object> map){
		JsonResult json = new JsonResult();
		String[] par = new String[]
		{ "auctionId","userId"};
		if (!ResultUtil.isPageDate(par, map)){
			return error700(json);
		}
		try {
			map.put("auctionId", Integer.valueOf(map.get("auctionId").toString()));
			map.put("userId", Integer.valueOf(map.get("userId").toString()));
			auctionService.saveBid(map);
			return success(json);
		}catch(SelfBidNotAllowedException e) {
			return error600(json, "不能拍买自己发布的拍卖");
		}catch(AlreadyHighException e) {
			json.setCode(-103);
			json.setMessage("已是最高出价者，不需要竞标");
			return json;
		}catch(NoMarginException e) {
			json.setCode(-102);
			json.setMessage("该用户未交保证金");
			return json;
		}
		 catch (Exception e) {
			e.printStackTrace();
			return error200(json);
		}
	}
}




