package com.ypkj.zubu.service.auction.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ypkj.zubu.service.auction.bean.Auction;
import com.ypkj.zubu.service.auction.bean.AuctionList;
import com.ypkj.zubu.service.auction.bean.AuctionRecord;
import com.ypkj.zubu.service.auction.bean.UserAuction;
import com.ypkj.zubu.service.auction.dao.AuctionDao;
import com.ypkj.zubu.service.auction.exception.AlreadyHighException;
import com.ypkj.zubu.service.auction.exception.NoMarginException;
import com.ypkj.zubu.service.auction.exception.SelfBidNotAllowedException;

@Service
public class AuctionService {

	@Autowired
	AuctionDao auctionDao;
	
	/**
	 * 报名申请
	 * @param auctionApply
	 * @throws Exception
	 */
	public void apply(Map<String,Object> auctionApply) throws Exception{
		auctionApply.put("applyTime", new Date());
		auctionDao.apply(auctionApply);
	}
	
	/**
	 * 拍卖列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<AuctionList> findAuctionList(Map<String, Object> map) throws Exception {
		int pageNo = Integer.valueOf(map.get("pageNo").toString());
		int pageSize = Integer.valueOf(map.get("pageSize").toString());
		int pageFrom = (pageNo-1) * pageSize;
		map.put("pageFrom", pageFrom);
		map.put("pageSize", pageSize);
		List<AuctionList> list = auctionDao.findAuctionList(map);
		return list;
	}
	
	public void saveBid(Map<String, Object> map) throws Exception{
		//检查是否是自己发起的拍卖
		Auction auction = auctionDao.findAuctionById(Integer.valueOf(map.get("auctionId").toString()));
		if(auction.getUserId().equals(Integer.valueOf(map.get("userId").toString()))){
			throw new SelfBidNotAllowedException();
		}
		//检查是否已是最高出价者
		List<AuctionRecord> listRecord = auctionDao.findRecordByAuction(map);
		Double highestPrice = new Double(0);
		if(listRecord!=null && listRecord.size()>0){
			Integer userId = listRecord.get(0).getUserId();
			if(userId.equals(map.get("userId"))){
				throw new AlreadyHighException();
			}
			highestPrice = listRecord.get(0).getPrice();
		}
		
		//检查是否缴保证金
		List<Map<String, Object>> listMargin = auctionDao.findOrderByUserAndAuction(map);
		if(listMargin==null || listMargin.size()==0){
			throw new NoMarginException();
		}
		
		//自动计算出当前竞标价并保存
		Double startPrice = highestPrice;
		if(startPrice.doubleValue() == 0) startPrice=auction.getStartPrice();
		Double nowPrice = startPrice + auction.getPremiumRange();
		
		Map<String, Object> recordParams = new HashMap<String, Object>();
		recordParams.put("auctionId", map.get("auctionId"));
		recordParams.put("userId", map.get("userId"));
		recordParams.put("price", nowPrice);
		recordParams.put("createTime", new Date());
		auctionDao.bid(recordParams);
		
		//更新用户拍卖消息
		UserAuction userAuction = auctionDao.findAuctionMsg(map);
		if(userAuction == null){
			Map<String, Object> msgParams = new HashMap<String, Object>();
			msgParams.put("auctionId", map.get("auctionId"));
			msgParams.put("userId", map.get("userId"));
			msgParams.put("type", 2);
			msgParams.put("status", UserAuction.STATUS_AUCT_SUCCESS);
			msgParams.put("content", UserAuction.CONTENT_SUCCESS.replace("%name%", auction.getAuctionName()));
			msgParams.put("updateTime", new Date());
			auctionDao.saveAuctionMsg(msgParams);
		}else{
			Map<String, Object> msgParams = new HashMap<String, Object>();
			msgParams.put("auctionId", map.get("auctionId"));
			msgParams.put("userId", map.get("userId"));
			msgParams.put("status", UserAuction.STATUS_AUCT_SUCCESS);
			msgParams.put("content", UserAuction.CONTENT_SUCCESS.replace("%name%", auction.getAuctionName()));
			msgParams.put("updateTime", new Date());
			auctionDao.updateAuctionMsg(msgParams);
		}
		
		Integer highestUserId = (listRecord!=null && listRecord.size()>0)?listRecord.get(0).getUserId() : null;
		int size = (listRecord!=null && listRecord.size()>0)?listRecord.size() : 0;
		this.updateNotifyMsg(map, highestUserId, size,auction);
	}
	
	/**
	 * 更新消息
	 * @param map
	 * @param highestUserId 之前出价最高者id
	 * @param sellerId 拍卖卖家id
	 * @param size 总共多少人拍买
	 * @throws Exception 
	 */
	private void updateNotifyMsg(Map<String, Object> map,Integer highestUserId,int size,Auction auction) throws Exception{
		//更新卖家消息
		Map<String, Object> msgParams = new HashMap<String, Object>();
		msgParams.put("auctionId", map.get("auctionId"));
		msgParams.put("userId", auction.getUserId());
		msgParams.put("content", UserAuction.CONTENT_ING.replace("%num%", String.valueOf(size)).replace("%name%", auction.getAuctionName()));
		msgParams.put("updateTime", new Date());
		auctionDao.updateAuctionMsg(msgParams);
		//更新第二名拍卖者消息
		if(highestUserId != null){
			msgParams = new HashMap<String, Object>();
			msgParams.put("auctionId", map.get("auctionId"));
			msgParams.put("userId", highestUserId);
			msgParams.put("content", UserAuction.CONTENT_OUT);
			msgParams.put("updateTime", new Date());
			auctionDao.updateAuctionMsg(msgParams);
		}
	}
}
