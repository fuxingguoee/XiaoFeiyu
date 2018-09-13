package com.il360.xiaofeiyu.model.hua;

import java.io.Serializable;
import java.util.List;

public class ArrayOfPayWay implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String desc;
	private List<PayWay> result;

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

	public List<PayWay> getResult() {
		return result;
	}

	public void setResult(List<PayWay> result) {
		this.result = result;
	}

}
