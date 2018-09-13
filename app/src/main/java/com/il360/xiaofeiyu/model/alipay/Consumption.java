package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;

public class Consumption implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rdsToken;
	private String rdsUa;
	private String beginDate;
	private String beginTime;
	private String endDate;
	private String endTime;
	private String dateRange;
	private String status;
	private String keyword;
	private String keyValue;
	private String dateType;
	private String minAmount;
	private String maxAmount;
	private String fundFlow;
	private String tradeType;
	private String categoryId;
	private String pageNum;

	public String getRdsToken() {
		return rdsToken;
	}

	public void setRdsToken(String rdsToken) {
		this.rdsToken = rdsToken;
	}

	public String getRdsUa() {
		return rdsUa;
	}

	public void setRdsUa(String rdsUa) {
		this.rdsUa = rdsUa;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
	}

	public String getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getFundFlow() {
		return fundFlow;
	}

	public void setFundFlow(String fundFlow) {
		this.fundFlow = fundFlow;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

}
