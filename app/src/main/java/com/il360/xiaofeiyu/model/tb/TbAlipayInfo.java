package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

public class TbAlipayInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String account;
	private String realName;
	private String idCard;
	private String authStatus;
	private Integer accBal;
	private Integer ebaoBal;
	private Integer totalEarningsAmt;
	private Integer hbUsableAmt;
	private Integer hbAmount;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	public Integer getAccBal() {
		return accBal;
	}
	public void setAccBal(Integer accBal) {
		this.accBal = accBal;
	}
	public Integer getEbaoBal() {
		return ebaoBal;
	}
	public void setEbaoBal(Integer ebaoBal) {
		this.ebaoBal = ebaoBal;
	}
	public Integer getTotalEarningsAmt() {
		return totalEarningsAmt;
	}
	public void setTotalEarningsAmt(Integer totalEarningsAmt) {
		this.totalEarningsAmt = totalEarningsAmt;
	}
	public Integer getHbUsableAmt() {
		return hbUsableAmt;
	}
	public void setHbUsableAmt(Integer hbUsableAmt) {
		this.hbUsableAmt = hbUsableAmt;
	}
	public Integer getHbAmount() {
		return hbAmount;
	}
	public void setHbAmount(Integer hbAmount) {
		this.hbAmount = hbAmount;
	}
	
	
	
}
