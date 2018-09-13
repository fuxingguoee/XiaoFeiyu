package com.il360.xiaofeiyu.model.address;

import java.io.Serializable;
import java.util.List;

public class ArrayOfAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String desc;
	private List<Address> result;

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

	public List<Address> getResult() {
		return result;
	}

	public void setResult(List<Address> result) {
		this.result = result;
	}

}
