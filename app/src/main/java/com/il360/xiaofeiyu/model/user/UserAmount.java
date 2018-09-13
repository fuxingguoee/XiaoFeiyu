package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserAmount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private BigDecimal allAmount;
	private BigDecimal invitationAmount;
	private BigDecimal useAmount;
	private String payPwd;//支付密码，1表示未设置，其他表示已设置
	private BigDecimal useInvitationAmount;//已提现金额

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(BigDecimal allAmount) {
		this.allAmount = allAmount;
	}

	public BigDecimal getInvitationAmount() {
		return invitationAmount;
	}

	public void setInvitationAmount(BigDecimal invitationAmount) {
		this.invitationAmount = invitationAmount;
	}

	public BigDecimal getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(BigDecimal useAmount) {
		this.useAmount = useAmount;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public BigDecimal getUseInvitationAmount() {
		return useInvitationAmount;
	}

	public void setUseInvitationAmount(BigDecimal useInvitationAmount) {
		this.useInvitationAmount = useInvitationAmount;
	}

}
