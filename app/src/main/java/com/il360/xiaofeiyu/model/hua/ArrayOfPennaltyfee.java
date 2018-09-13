package com.il360.xiaofeiyu.model.hua;

import java.io.Serializable;
import java.util.List;

public class ArrayOfPennaltyfee implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String desc;
	private List<CreditPenaltyfee> result;

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

	public List<CreditPenaltyfee> getResult() {
		return result;
	}

	public void setResult(List<CreditPenaltyfee> result) {
		this.result = result;
	}

}
