package com.il360.xiaofeiyu.model.recovery;

import java.io.Serializable;
import java.math.BigDecimal;

public class PromoteAmount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer type;
	private String orderNo;
	private BigDecimal orderAmount;
	private Integer userId;
	private BigDecimal amount;
	private Long createTime;
	private Integer bext1;
	private String bext2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getBext1() {
		return bext1;
	}

	public void setBext1(Integer bext1) {
		this.bext1 = bext1;
	}

	public String getBext2() {
		return bext2;
	}

	public void setBext2(String bext2) {
		this.bext2 = bext2;
	}

}
