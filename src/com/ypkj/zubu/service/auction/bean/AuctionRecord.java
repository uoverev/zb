package com.ypkj.zubu.service.auction.bean;

import java.util.Date;

public class AuctionRecord {

	private Integer auctionRecordId;
	
	private Integer auctionId;
	
	private Integer userId;
	
	private Double price;
	
	private Date createTime;

	public Integer getAuctionRecordId() {
		return auctionRecordId;
	}

	public void setAuctionRecordId(Integer auctionRecordId) {
		this.auctionRecordId = auctionRecordId;
	}

	public Integer getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Integer auctionId) {
		this.auctionId = auctionId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
