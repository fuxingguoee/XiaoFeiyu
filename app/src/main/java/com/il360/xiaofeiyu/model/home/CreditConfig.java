package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer creditConfigId;
	private Integer creditPeriodsDay;
	private Integer creditPeriodsNumber;
	private BigDecimal creditFee;
	private Integer creditPeriodsRate;
	private Integer creditDueRate;
	private BigDecimal creditAmount;
	
	public Integer getCreditConfigId() {
		return creditConfigId;
	}
	public void setCreditConfigId(Integer creditConfigId) {
		this.creditConfigId = creditConfigId;
	}
	public Integer getCreditPeriodsDay() {
		return creditPeriodsDay;
	}
	public void setCreditPeriodsDay(Integer creditPeriodsDay) {
		this.creditPeriodsDay = creditPeriodsDay;
	}
	public Integer getCreditPeriodsNumber() {
		return creditPeriodsNumber;
	}
	public void setCreditPeriodsNumber(Integer creditPeriodsNumber) {
		this.creditPeriodsNumber = creditPeriodsNumber;
	}
	public BigDecimal getCreditFee() {
		return creditFee;
	}
	public void setCreditFee(BigDecimal creditFee) {
		this.creditFee = creditFee;
	}
	public Integer getCreditPeriodsRate() {
		return creditPeriodsRate;
	}
	public void setCreditPeriodsRate(Integer creditPeriodsRate) {
		this.creditPeriodsRate = creditPeriodsRate;
	}
	public Integer getCreditDueRate() {
		return creditDueRate;
	}
	public void setCreditDueRate(Integer creditDueRate) {
		this.creditDueRate = creditDueRate;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	
	

}
