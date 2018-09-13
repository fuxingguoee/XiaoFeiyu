package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class LeaseOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer leaseId;//租赁Id
	private String leaseOrderNo;//租赁订单号
	private Integer valuationId;//评估Id
	private Integer goodsId;//商品id
	private Integer userId;//用户id
	private String serialNumber;//手机序列号
	private String imei;//imei号
	private String phonePic;
	private String txyPhonePic;//腾讯云图片地址
	private String txyUrl;//腾讯云签名地址
	private BigDecimal money;//最终评估金额
	private BigDecimal deposit;//押金
	private BigDecimal firstDayRent;//首日租金
	private BigDecimal getMoney;//用户到手金额
	private String createTime;//创建时间 
	private String creater;
	private String checkTime;//审核通过时间 
	private String expireTime;//租赁到期时间 
	private String penaltyRate;
	private String rentFee;
	private BigDecimal allOverdueFee;//滞纳金
	private Integer status;//状态 0审核中 -1未通过 1通过 2已赎回
	private String goodsSysDetail;
	private String ext1;
	private String ext2;
	private String isEverYuqi;//是否逾期过，1是0否
	private String backDeposit ;//)是否强制退还押金：0否1是

	public Integer getLeaseId() {
		return leaseId;
	}
	public void setLeaseId(Integer leaseId) {
		this.leaseId = leaseId;
	}
	public String getLeaseOrderNo() {
		return leaseOrderNo;
	}
	public void setLeaseOrderNo(String leaseOrderNo) {
		this.leaseOrderNo = leaseOrderNo;
	}
	public Integer getValuationId() {
		return valuationId;
	}
	public void setValuationId(Integer valuationId) {
		this.valuationId = valuationId;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getPhonePic() {
		return phonePic;
	}
	public void setPhonePic(String phonePic) {
		this.phonePic = phonePic;
	}
	public String getTxyPhonePic() {
		return txyPhonePic;
	}
	public void setTxyPhonePic(String txyPhonePic) {
		this.txyPhonePic = txyPhonePic;
	}
	public String getTxyUrl() {
		return txyUrl;
	}
	public void setTxyUrl(String txyUrl) {
		this.txyUrl = txyUrl;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	public BigDecimal getFirstDayRent() {
		return firstDayRent;
	}
	public void setFirstDayRent(BigDecimal firstDayRent) {
		this.firstDayRent = firstDayRent;
	}
	public BigDecimal getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(BigDecimal getMoney) {
		this.getMoney = getMoney;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	public String getPenaltyRate() {
		return penaltyRate;
	}
	public void setPenaltyRate(String penaltyRate) {
		this.penaltyRate = penaltyRate;
	}
	public String getRentFee() {
		return rentFee;
	}
	public void setRentFee(String rentFee) {
		this.rentFee = rentFee;
	}
	public BigDecimal getAllOverdueFee() {
		return allOverdueFee;
	}
	public void setAllOverdueFee(BigDecimal allOverdueFee) {
		this.allOverdueFee = allOverdueFee;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getGoodsSysDetail() {
		return goodsSysDetail;
	}
	public void setGoodsSysDetail(String goodsSysDetail) {
		this.goodsSysDetail = goodsSysDetail;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getIsEverYuqi() {
		return isEverYuqi;
	}

	public void setIsEverYuqi(String isEverYuqi) {
		this.isEverYuqi = isEverYuqi;
	}

	public String getBackDeposit() {
		return backDeposit;
	}

	public void setBackDeposit(String backDeposit) {
		this.backDeposit = backDeposit;
	}
	
}
