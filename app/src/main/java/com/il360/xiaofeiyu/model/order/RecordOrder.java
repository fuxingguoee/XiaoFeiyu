package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;

public class RecordOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String upOrderId;//支付订Id
	private String orderNo;//订单号
	private String orderSendTime;
	private String orderSendAmt;
	private String orderSendDesc;
	private String orderSendTn;
	private String orderResultTime;
	private String orderResultCode;
	private String orderResultDesc;
	private String orderResultSysReserved;
	private String orderResultQn;
	private String status;
	private String busType;
	private String bizFrom;
	private String userId;
	private String channelType;
	private String relateId;
	private String number;
	private String startTime;
	private String endTime;
	private String cardName;
	private String cardNo;
	private String rewardAmount;
	private String payFeeOrderNo;
	private String type;
	private String fee;
	private String startDay;
	private String endDay;
	private String overFee;
	private String rentFee;
	private String overReduction;
	private String rentReduction;
	private String stagesNumber;
	private String money;
	private String getMoney;
	
	public String getUpOrderId() {
		return upOrderId;
	}
	public void setUpOrderId(String upOrderId) {
		this.upOrderId = upOrderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderSendTime() {
		return orderSendTime;
	}
	public void setOrderSendTime(String orderSendTime) {
		this.orderSendTime = orderSendTime;
	}
	public String getOrderSendAmt() {
		return orderSendAmt;
	}
	public void setOrderSendAmt(String orderSendAmt) {
		this.orderSendAmt = orderSendAmt;
	}
	public String getOrderSendDesc() {
		return orderSendDesc;
	}
	public void setOrderSendDesc(String orderSendDesc) {
		this.orderSendDesc = orderSendDesc;
	}
	public String getOrderSendTn() {
		return orderSendTn;
	}
	public void setOrderSendTn(String orderSendTn) {
		this.orderSendTn = orderSendTn;
	}
	public String getOrderResultTime() {
		return orderResultTime;
	}
	public void setOrderResultTime(String orderResultTime) {
		this.orderResultTime = orderResultTime;
	}
	public String getOrderResultCode() {
		return orderResultCode;
	}
	public void setOrderResultCode(String orderResultCode) {
		this.orderResultCode = orderResultCode;
	}
	public String getOrderResultDesc() {
		return orderResultDesc;
	}
	public void setOrderResultDesc(String orderResultDesc) {
		this.orderResultDesc = orderResultDesc;
	}
	public String getOrderResultSysReserved() {
		return orderResultSysReserved;
	}
	public void setOrderResultSysReserved(String orderResultSysReserved) {
		this.orderResultSysReserved = orderResultSysReserved;
	}
	public String getOrderResultQn() {
		return orderResultQn;
	}
	public void setOrderResultQn(String orderResultQn) {
		this.orderResultQn = orderResultQn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBizFrom() {
		return bizFrom;
	}
	public void setBizFrom(String bizFrom) {
		this.bizFrom = bizFrom;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getRelateId() {
		return relateId;
	}
	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getRewardAmount() {
		return rewardAmount;
	}
	public void setRewardAmount(String rewardAmount) {
		this.rewardAmount = rewardAmount;
	}
	public String getPayFeeOrderNo() {
		return payFeeOrderNo;
	}
	public void setPayFeeOrderNo(String payFeeOrderNo) {
		this.payFeeOrderNo = payFeeOrderNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public String getOverFee() {
		return overFee;
	}
	public void setOverFee(String overFee) {
		this.overFee = overFee;
	}
	public String getRentFee() {
		return rentFee;
	}
	public void setRentFee(String rentFee) {
		this.rentFee = rentFee;
	}
	public String getOverReduction() {
		return overReduction;
	}
	public void setOverReduction(String overReduction) {
		this.overReduction = overReduction;
	}
	public String getRentReduction() {
		return rentReduction;
	}
	public void setRentReduction(String rentReduction) {
		this.rentReduction = rentReduction;
	}
	public String getStagesNumber() {
		return stagesNumber;
	}
	public void setStagesNumber(String stagesNumber) {
		this.stagesNumber = stagesNumber;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}
	
}
