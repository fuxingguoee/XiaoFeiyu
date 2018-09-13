package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;

public class OrderRecovery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderNo;
	private String recoveryDesc;
	private String expressNo;
	private String expressName;
	private String phone;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRecoveryDesc() {
		return recoveryDesc;
	}

	public void setRecoveryDesc(String recoveryDesc) {
		this.recoveryDesc = recoveryDesc;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
