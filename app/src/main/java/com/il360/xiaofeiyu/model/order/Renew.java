package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;

public class Renew implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**赎回天数*/
	private String days;
	/**金额*/
	private String amount;
	/**是否选择*/
	private boolean checked;
	
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
