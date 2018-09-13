package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

public class BasicInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private String userId;
	private String userName;
	private String phoneNo;
	private String realName;
	private String idCard;
	private String loginEmail;
	private String naughtyValue;
	private int fastRefundAmt;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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
	public String getLoginEmail() {
		return loginEmail;
	}
	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}
	public String getNaughtyValue() {
		return naughtyValue;
	}
	public void setNaughtyValue(String naughtyValue) {
		this.naughtyValue = naughtyValue;
	}
	public int getFastRefundAmt() {
		return fastRefundAmt;
	}
	public void setFastRefundAmt(int fastRefundAmt) {
		this.fastRefundAmt = fastRefundAmt;
	}
	
}
