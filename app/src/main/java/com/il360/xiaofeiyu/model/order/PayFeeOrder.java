package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayFeeOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer payFeeId;//缴费订单id
	private Integer userId;//用户ID
	private String payFeeOrderNo;//缴费订单号
	private Integer type;//1续费 2赎回 3超时缴费
	private Integer stageId;//续费套餐id
	private BigDecimal fee;//费用金额
	private Integer status;//0未缴费 1已缴费
	private String createTime;//创建时间
	private BigDecimal overFee;//超时费
	
	public BigDecimal getOverFee() {
		return overFee;
	}
	public void setOverFee(BigDecimal overFee) {
		this.overFee = overFee;
	}
	public Integer getPayFeeId() {
		return payFeeId;
	}
	public void setPayFeeId(Integer payFeeId) {
		this.payFeeId = payFeeId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPayFeeOrderNo() {
		return payFeeOrderNo;
	}
	public void setPayFeeOrderNo(String payFeeOrderNo) {
		this.payFeeOrderNo = payFeeOrderNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStageId() {
		return stageId;
	}
	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
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
