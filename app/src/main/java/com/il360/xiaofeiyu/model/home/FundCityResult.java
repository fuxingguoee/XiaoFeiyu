package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;
import java.util.List;

public class FundCityResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String desc;
	private List<FundProvince> result;

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

	public List<FundProvince> getResult() {
		return result;
	}

	public void setResult(List<FundProvince> result) {
		this.result = result;
	}

}
