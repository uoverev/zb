package com.ypkj.zubu.service.auction.bean;

import java.util.Date;

public class UserAuction {
	
	public static final Integer STATUS_AUCT_ING = 101;
	public static final Integer STATUS_AUCT_END = 102;
	public static final Integer STATUS_AUCT_SUCCESS = 201;
	public static final Integer STATUS_AUCT_OUT = 202;
	
	public static final String CONTENT_SUCCESS = "恭喜您成功竞拍了%name%";
	public static final String CONTENT_ING = "已有%num%人出价拍卖您的%name%";
	public static final String CONTENT_OUT = "您已出局";

	private Integer id;
	
	private Integer auctionId;
	
	private Integer userId;
	
	private Integer type;
	
	private Integer status;
	
	private String content;
	
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
