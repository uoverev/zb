package com.ypkj.zubu.service.auction.bean;

import java.util.Date;

public class Auction {

	private Integer auctionId;
	
	private String auctionName;
	
	private Double startPrice;
	
	private Double bond;
	
	private Double premiumRange;
	
	private String auctionContent;
	
	private Integer userId;
	
	private Integer bidUserId;
	
	private Date createTime;
	
	private Date startTime;
	
	private Date endTime;
	
	private String ind_belong;
	
	private Integer applyNum;
	
	private Integer bidNum;
	
	private String imgUrl;

	public Integer getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Integer auctionId) {
		this.auctionId = auctionId;
	}

	public String getAuctionName() {
		return auctionName;
	}

	public void setAuctionName(String auctionName) {
		this.auctionName = auctionName;
	}

	public Double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}

	public Double getBond() {
		return bond;
	}

	public void setBond(Double bond) {
		this.bond = bond;
	}

	public Double getPremiumRange() {
		return premiumRange;
	}

	public void setPremiumRange(Double premiumRange) {
		this.premiumRange = premiumRange;
	}

	public String getAuctionContent() {
		return auctionContent;
	}

	public void setAuctionContent(String auctionContent) {
		this.auctionContent = auctionContent;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBidUserId() {
		return bidUserId;
	}

	public void setBidUserId(Integer bidUserId) {
		this.bidUserId = bidUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getInd_belong() {
		return ind_belong;
	}

	public void setInd_belong(String ind_belong) {
		this.ind_belong = ind_belong;
	}

	public Integer getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}

	public Integer getBidNum() {
		return bidNum;
	}

	public void setBidNum(Integer bidNum) {
		this.bidNum = bidNum;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
}
