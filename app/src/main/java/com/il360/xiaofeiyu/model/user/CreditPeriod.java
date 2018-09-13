package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CreditPeriod
 *
 */
public class CreditPeriod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 借款订单id **/
	private Integer creditOrderId;
	/** 还款期号 **/
	private Integer periodsId;
	/** 还款到期时间 **/
	private String periodDate;
	/** 当期还款金额 **/
	private BigDecimal periodsAmount;
	/** 状态 0:新建 1:已还款 **/
	private Integer status;
	/** 逾期费 **/
	private BigDecimal dueFee;
	/** 还款时间 **/
	private Integer repaymentTime;
	private Integer userId;

	/**
	 * 还款结束期数
	 */
	private Integer periodsEnd;
	/**
	 * 当前要还款的记录数 统计查询用到
	 */
	private Integer repayCount;
	/**
	 * 总的要还款的金额 统计查询用到
	 */
	private BigDecimal repayAmount;

	/**
	 * 总的贷款金额
	 */
	private BigDecimal amount;
	/**
	 * 贷款时间
	 */
	private String createTime;
	
	private int choisePeriod;//选中的期数

	public void setCreditOrderId(Integer creditOrderId) {
		this.creditOrderId = creditOrderId;
	}

	public Integer getCreditOrderId() {
		return this.creditOrderId;
	}

	public void setPeriodsId(Integer periodsId) {
		this.periodsId = periodsId;
	}

	public Integer getPeriodsId() {
		return this.periodsId;
	}

	public void setPeriodDate(String periodDate) {
		this.periodDate = periodDate;
	}

	public String getPeriodDate() {
		return this.periodDate;
	}

	public void setPeriodsAmount(BigDecimal periodsAmount) {
		this.periodsAmount = periodsAmount;
	}

	public BigDecimal getPeriodsAmount() {
		return this.periodsAmount;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setDueFee(BigDecimal dueFee) {
		this.dueFee = dueFee;
	}

	public BigDecimal getDueFee() {
		return this.dueFee;
	}

	public void setRepaymentTime(Integer repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	public Integer getRepaymentTime() {
		return this.repaymentTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPeriodsEnd() {
		return periodsEnd;
	}

	public void setPeriodsEnd(Integer periodsEnd) {
		this.periodsEnd = periodsEnd;
	}

	public Integer getRepayCount() {
		return repayCount;
	}

	public void setRepayCount(Integer repayCount) {
		this.repayCount = repayCount;
	}

	public BigDecimal getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getChoisePeriod() {
		return choisePeriod;
	}

	public void setChoisePeriod(int choisePeriod) {
		this.choisePeriod = choisePeriod;
	}

}
