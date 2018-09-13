package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.util.List;

public class ArrayOfRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String desc;
	private List<Record> result;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Record> getResult() {
		return result;
	}

	public void setResult(List<Record> result) {
		this.result = result;
	}

}
