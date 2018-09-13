package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;

public class AliBank implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cardSuffix;
	private String bankId;
	private String userName;
	private String bankName;
	private String cardTypeName;

	public String getCardSuffix() {
		return cardSuffix;
	}

	public void setCardSuffix(String cardSuffix) {
		this.cardSuffix = cardSuffix;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

}
