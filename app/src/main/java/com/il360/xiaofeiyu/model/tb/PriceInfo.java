package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

/**子订单单价和原价*/
public class PriceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String original;
	private String realTotal;
	
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getRealTotal() {
		return realTotal;
	}
	public void setRealTotal(String realTotal) {
		this.realTotal = realTotal;
	}
}
