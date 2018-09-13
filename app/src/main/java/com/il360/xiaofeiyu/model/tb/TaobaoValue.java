package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;

public class TaobaoValue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String code;
	private ValueData data;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ValueData getData() {
		return data;
	}
	public void setData(ValueData data) {
		this.data = data;
	}

}
