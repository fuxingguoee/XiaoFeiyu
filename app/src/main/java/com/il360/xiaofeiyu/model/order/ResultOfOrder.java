package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;

public class ResultOfOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String desc;
	private Order result;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Order getResult() {
		return result;
	}
	public void setResult(Order result) {
		this.result = result;
	}
	
	
	
}
