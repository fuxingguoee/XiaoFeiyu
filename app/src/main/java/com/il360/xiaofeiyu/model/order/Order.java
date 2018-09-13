package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String goodsId;//商品Id
	private String operator;//手机运营商
	private String position;//定位地址
	private Integer charge;//充电情况
	private Integer appearance;//外观描述
	private Integer repair;//维修情况 
	private String money;//手机价格
	private Integer status;
	private String createTime;
	private String valuationId;
	private String orderNo;
	private String userId;
	private String ext1;
	private String ext2;
	private String city;
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getValuationId() {
		return valuationId;
	}

	public void setValuationId(String valuationId) {
		this.valuationId = valuationId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getCharge() {
		return charge;
	}

	public void setCharge(Integer charge) {
		this.charge = charge;
	}

	public Integer getAppearance() {
		return appearance;
	}

	public void setAppearance(Integer appearance) {
		this.appearance = appearance;
	}

	public Integer getRepair() {
		return repair;
	}

	public void setRepair(Integer repair) {
		this.repair = repair;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
