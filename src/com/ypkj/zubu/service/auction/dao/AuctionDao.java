package com.ypkj.zubu.service.auction.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.service.auction.bean.Auction;
import com.ypkj.zubu.service.auction.bean.AuctionList;
import com.ypkj.zubu.service.auction.bean.AuctionRecord;
import com.ypkj.zubu.service.auction.bean.UserAuction;

@Repository
public class AuctionDao {

	@Autowired
	DaoSupport daoSupport;
	
	/**
	 * 申请拍卖
	 * @param auctionApply
	 * @throws Exception
	 */
	public void apply(Map<String, Object> auctionApply) throws Exception{
		daoSupport.save("AuctionMapper.saveApply", auctionApply);
	}
	
	/**
	 * 查询拍卖列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<AuctionList> findAuctionList(Map<String, Object> map) throws Exception{
		List<AuctionList> list = (List<AuctionList>) daoSupport.findForList("AuctionMapper.aucList", map);
		return list;
	}
	
	/**
	 * 查询当前拍卖最高出价者
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<AuctionRecord> findRecordByAuction(Map<String,Object> map) throws Exception{
		List<AuctionRecord> recordList = (List<AuctionRecord>) daoSupport.findForList("AuctionMapper.selectHighest", map);
		return recordList;
	} 
	
	/**
	 * 查询当前用户当前拍卖保证金情况
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOrderByUserAndAuction(Map<String, Object> map) throws Exception{
		List<Map<String, Object>> list = (List<Map<String, Object>>) daoSupport.findForList("AuctionMapper.checkMargin", map); 
		return list;
	}
	
	public Auction findAuctionById(Integer id) throws Exception{
		Auction auction = (Auction) daoSupport.findForObject("AuctionMapper.findAuction", id);
		return auction;
	}
	
	public void bid (Map<String, Object> map) throws Exception{
		daoSupport.save("AuctionMapper.saveRecord", map);
		daoSupport.update("AuctionMapper.plusBidNum", map.get("auctionId"));
	}
	
	/**
	 * 保存拍卖消息
	 * @param map
	 * @throws Exception
	 */
	public void saveAuctionMsg(Map<String, Object> map) throws Exception{
		daoSupport.save("AuctionMapper.saveMsg", map);
	}
	
	public void updateAuctionMsg(Map<String, Object> map) throws Exception{
		daoSupport.update("AuctionMapper.updateMsg", map);
	}
	public UserAuction findAuctionMsg(Map<String, Object> map) throws Exception{
		UserAuction userAuction = (UserAuction) daoSupport.findForObject("AuctionMapper.selectMsg", map);
		return userAuction;
	}
}
