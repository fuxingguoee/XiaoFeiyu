package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;
import java.math.BigDecimal;

public class AliUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	private String idNum;
	private String email;
	private String phone;
	private String tbAccount;
	private BigDecimal amount;
	private BigDecimal usedAmount;//已用额度
	private BigDecimal billAmount;//当月账单
	private BigDecimal accBal;
	private BigDecimal ebaoBal;
	private String regTime;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTbAccount() {
		return tbAccount;
	}

	public void setTbAccount(String tbAccount) {
		this.tbAccount = tbAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAccBal() {
		return accBal;
	}

	public void setAccBal(BigDecimal accBal) {
		this.accBal = accBal;
	}

	public BigDecimal getEbaoBal() {
		return ebaoBal;
	}

	public void setEbaoBal(BigDecimal ebaoBal) {
		this.ebaoBal = ebaoBal;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public BigDecimal getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(BigDecimal usedAmount) {
		this.usedAmount = usedAmount;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

}
