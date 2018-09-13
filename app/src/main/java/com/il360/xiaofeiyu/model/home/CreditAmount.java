package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;

public class CreditAmount implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer totalScore;
	private Integer amount;
	private Integer status;
	private String expireDate;
	private String createTime;
	private String updateTime;
	private String bizExt1;
	private String bizExt2;
	private String bizExt3;
	private String bizExt4;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getBizExt1() {
		return bizExt1;
	}
	public void setBizExt1(String bizExt1) {
		this.bizExt1 = bizExt1;
	}
	public String getBizExt2() {
		return bizExt2;
	}
	public void setBizExt2(String bizExt2) {
		this.bizExt2 = bizExt2;
	}
	public String getBizExt3() {
		return bizExt3;
	}
	public void setBizExt3(String bizExt3) {
		this.bizExt3 = bizExt3;
	}
	public String getBizExt4() {
		return bizExt4;
	}
	public void setBizExt4(String bizExt4) {
		this.bizExt4 = bizExt4;
	}
}
