package com.ypkj.zubu.service.auction.bean;

import java.util.Date;

public class AuctionList {

	private Integer auctionApplyId;
	
	private Integer userId;
	
	private String name;
	
	private String company;
	
	private String position;
	
	private Integer phone;
	
	private Date applyTime;

	public Integer getAuctionApplyId() {
		return auctionApplyId;
	}

	public void setAuctionApplyId(Integer auctionApplyId) {
		this.auctionApplyId = auctionApplyId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	
	
}
