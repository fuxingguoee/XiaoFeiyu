package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

/**账户余额,余额宝信息具体内容*/
public class TbAlipayInfo2Data implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String balance;//账户余额
	private String sign;
	private String totalProfit;//余额宝历史收益
	private String totalQuotient;//余额宝余额
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(String totalProfit) {
		this.totalProfit = totalProfit;
	}
	public String getTotalQuotient() {
		return totalQuotient;
	}
	public void setTotalQuotient(String totalQuotient) {
		this.totalQuotient = totalQuotient;
	}
	
	
	
}
