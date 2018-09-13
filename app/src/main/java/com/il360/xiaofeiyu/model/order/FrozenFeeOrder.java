package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.math.BigDecimal;

public class FrozenFeeOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer frozenId;//缴费订单id
	private Integer userId;//用户ID
	private String frozenOrderNo;//缴费订单号
	private BigDecimal totalFee;
	private BigDecimal thisFee;
	private String status;//费用金额
	private String creator;//0未缴费 1已缴费
	private String createTime;//创建时间

	public Integer getFrozenId() {
		return frozenId;
	}

	public void setFrozenId(Integer frozenId) {
		this.frozenId = frozenId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFrozenOrderNo() {
		return frozenOrderNo;
	}

	public void setFrozenOrderNo(String frozenOrderNo) {
		this.frozenOrderNo = frozenOrderNo;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getThisFee() {
		return thisFee;
	}

	public void setThisFee(BigDecimal thisFee) {
		this.thisFee = thisFee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}



	
	
}
