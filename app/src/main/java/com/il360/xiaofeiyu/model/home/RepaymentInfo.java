package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;
import java.math.BigDecimal;

public class RepaymentInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal repayAmount;
	private BigDecimal repayDueFee;
	private Integer repayCount;

	public BigDecimal getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}

	public BigDecimal getRepayDueFee() {
		return repayDueFee;
	}

	public void setRepayDueFee(BigDecimal repayDueFee) {
		this.repayDueFee = repayDueFee;
	}

	public Integer getRepayCount() {
		return repayCount;
	}

	public void setRepayCount(Integer repayCount) {
		this.repayCount = repayCount;
	}

}
