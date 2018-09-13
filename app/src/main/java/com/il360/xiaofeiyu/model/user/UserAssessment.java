package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

public class UserAssessment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**用户**/
	private Integer userId;
	/**实名认证评估分**/
	private Integer regScore;
	/**银行四要素评估分**/
	private Integer bankScore;
	/**运营商评估分**/
	private Integer carrierScore;
	/**账单认证评估分**/
	private Integer billScore;
	/**认证评估总分**/
	private Integer allScore;
	/**创建时间**/
	private String createTime;
	/**更新时间**/
	private String updateTime;
	
	private String money;//可贷款金额
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setRegScore(Integer regScore) {
		this.regScore = regScore;
	}
	
	public Integer getRegScore() {
		return this.regScore;
	}
	
	public void setBankScore(Integer bankScore) {
		this.bankScore = bankScore;
	}
	
	public Integer getBankScore() {
		return this.bankScore;
	}
	
	public void setCarrierScore(Integer carrierScore) {
		this.carrierScore = carrierScore;
	}
	
	public Integer getCarrierScore() {
		return this.carrierScore;
	}
	
	public void setBillScore(Integer billScore) {
		this.billScore = billScore;
	}
	
	public Integer getBillScore() {
		return this.billScore;
	}
	
	public void setAllScore(Integer allScore) {
		this.allScore = allScore;
	}
	
	public Integer getAllScore() {
		return this.allScore;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getCreateTime() {
		return this.createTime;
	}
	
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getUpdateTime() {
		return this.updateTime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
}
