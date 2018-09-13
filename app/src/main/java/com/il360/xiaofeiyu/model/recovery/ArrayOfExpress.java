package com.il360.xiaofeiyu.model.recovery;

import java.io.Serializable;
import java.util.List;

public class ArrayOfExpress implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String desc;
	private List<Express> result;

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

	public List<Express> getResult() {
		return result;
	}

	public void setResult(List<Express> result) {
		this.result = result;
	}

}
