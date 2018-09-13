package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.util.List;

public class ArrayOfCreditPeriod implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int  code;
	private String desc;
	private List<CreditPeriod> result;
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
	public List<CreditPeriod> getResult() {
		return result;
	}
	public void setResult(List<CreditPeriod> result) {
		this.result = result;
	}
	
	
}
