package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserWithdrawals implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 提现id **/
	private Integer withdrawalsId;
	/** 用户id **/
	private Integer userId;
	/** 提现金额 **/
	private BigDecimal amount;
	/** 状态0体现中1成功-1失败 **/
	private Integer status;
	/** 银行卡号 **/
	private String bankNo;
	/** 银行名称 **/
	private String bankName;
	/** 持卡人 **/
	private String name;
	/** 创建时间 **/
	private Long createTime;
	/** 更新时间 **/
	private Long updateTime;
	/** 操作员 **/
	private String operatorName;
	/** 操作员id **/
	private Integer operatorId;

	public Integer getWithdrawalsId() {
		return withdrawalsId;
	}

	public void setWithdrawalsId(Integer withdrawalsId) {
		this.withdrawalsId = withdrawalsId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

}
