package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

public class Taobao1DeliveryInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String logisticsName;//快递名称
	private String sellerNick;//卖家淘宝昵称
	private String tspInfo;
	private String address;
	private String showLogistics;
	private String shipType;
	private String companyUrl;
	private String logisticsNum;//快递单号
	private String showTSP;
	private String asyncLogisticsUrl;//物流详情链接
	
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	public String getSellerNick() {
		return sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}
	public String getTspInfo() {
		return tspInfo;
	}
	public void setTspInfo(String tspInfo) {
		this.tspInfo = tspInfo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getShowLogistics() {
		return showLogistics;
	}
	public void setShowLogistics(String showLogistics) {
		this.showLogistics = showLogistics;
	}
	public String getShipType() {
		return shipType;
	}
	public void setShipType(String shipType) {
		this.shipType = shipType;
	}
	public String getCompanyUrl() {
		return companyUrl;
	}
	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}
	public String getLogisticsNum() {
		return logisticsNum;
	}
	public void setLogisticsNum(String logisticsNum) {
		this.logisticsNum = logisticsNum;
	}
	public String getShowTSP() {
		return showTSP;
	}
	public void setShowTSP(String showTSP) {
		this.showTSP = showTSP;
	}
	public String getAsyncLogisticsUrl() {
		return asyncLogisticsUrl;
	}
	public void setAsyncLogisticsUrl(String asyncLogisticsUrl) {
		this.asyncLogisticsUrl = asyncLogisticsUrl;
	}
	
	
	

}
