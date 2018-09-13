package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

/** 淘宝订单接收类 */
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String tbOrderId;
	private String orderTime;
	private int amount;
	private String statusName;
	private String shopName;
	private String itemType;
	private String paymentMethod;
	
	public String getTbOrderId() {
		return tbOrderId;
	}

	public void setTbOrderId(String tbOrderId) {
		this.tbOrderId = tbOrderId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
