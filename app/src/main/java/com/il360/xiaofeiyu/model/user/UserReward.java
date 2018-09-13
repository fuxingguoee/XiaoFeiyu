package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserReward implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 用户id **/
	private Integer userId;
	/** 被推荐用户id **/
	private Integer buyUserId;
	/** 订单号 **/
	private String orderNo;
	/** 奖励金 **/
	private BigDecimal amount;
	/** 状态 **/
	private String status;
	/** 创建时间 **/
	private Long createTime;
	/** 更新时间 **/
	private Long updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBuyUserId() {
		return buyUserId;
	}

	public void setBuyUserId(Integer buyUserId) {
		this.buyUserId = buyUserId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

}
