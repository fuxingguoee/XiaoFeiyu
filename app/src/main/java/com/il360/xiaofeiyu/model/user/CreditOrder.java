package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CreditOrder
 *
 */
public class CreditOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 借款订单id **/
	private Integer creditOrderId;
	/** 用户id **/
	private Integer userId;
	/** 借款金额 **/
	private BigDecimal amount;
	/** 手续费 **/
	private BigDecimal fee;
	/** 借款周期 默认 1天/期 **/
	private Integer periodsDay;
	/** 借款期数 365期 **/
	private Integer periodsNumber;
	/** 每期还款金额 万分比 **/
	private Integer periodsRate;
	/** 每期逾期费率 万分比 **/
	private Integer dueRate;
	/** 开始日期 **/
	private String startday;
	/** 结束日期 **/
	private String endday;
	/** 创建时间 **/
	private String createTime;
	/** 状态 0:正常结束,1:逾期未还，2逾期还清，3：违约，4贷款中,5审核不通过，不予贷款,9:申请贷款中 **/
	private Integer status;
	/** 已还款总期数 **/
	private Integer repaymentPriod;
	/** 已偿还金额 **/
	private BigDecimal repaymentAmount;
	/** 还款时间 坏账时为设置坏账时间 **/
	private String repaymentTime;
	/** modifier **/
	private String modifier;
	/** modifytime **/
	private String modifytime;
	/** 0：批处理程序未处理,1：批处理程序已经处理 **/
	private Integer ext1;

	public void setCreditOrderId(Integer creditOrderId) {
		this.creditOrderId = creditOrderId;
	}

	public Integer getCreditOrderId() {
		return this.creditOrderId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getFee() {
		return this.fee;
	}

	public void setPeriodsDay(Integer periodsDay) {
		this.periodsDay = periodsDay;
	}

	public Integer getPeriodsDay() {
		return this.periodsDay;
	}

	public void setPeriodsNumber(Integer periodsNumber) {
		this.periodsNumber = periodsNumber;
	}

	public Integer getPeriodsNumber() {
		return this.periodsNumber;
	}

	public void setPeriodsRate(Integer periodsRate) {
		this.periodsRate = periodsRate;
	}

	public Integer getPeriodsRate() {
		return this.periodsRate;
	}

	public void setDueRate(Integer dueRate) {
		this.dueRate = dueRate;
	}

	public Integer getDueRate() {
		return this.dueRate;
	}

	public void setStartday(String startday) {
		this.startday = startday;
	}

	public String getStartday() {
		return this.startday;
	}

	public void setEndday(String endday) {
		this.endday = endday;
	}

	public String getEndday() {
		return this.endday;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setRepaymentPriod(Integer repaymentPriod) {
		this.repaymentPriod = repaymentPriod;
	}

	public Integer getRepaymentPriod() {
		return this.repaymentPriod;
	}

	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}

	public BigDecimal getRepaymentAmount() {
		return this.repaymentAmount;
	}

	public void setRepaymentTime(String repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	public String getRepaymentTime() {
		return this.repaymentTime;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getModifier() {
		return this.modifier;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	public String getModifytime() {
		return this.modifytime;
	}

	public void setExt1(Integer ext1) {
		this.ext1 = ext1;
	}

	public Integer getExt1() {
		return this.ext1;
	}
}
