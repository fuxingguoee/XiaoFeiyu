package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;

public class RemainTimes implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String desc;
	private Integer rmTimes;
	private Integer maxTimes;
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
	public Integer getRmTimes() {
		return rmTimes;
	}
	public void setRmTimes(Integer rmTimes) {
		this.rmTimes = rmTimes;
	}
	public Integer getMaxTimes() {
		return maxTimes;
	}
	public void setMaxTimes(Integer maxTimes) {
		this.maxTimes = maxTimes;
	}
	
}
