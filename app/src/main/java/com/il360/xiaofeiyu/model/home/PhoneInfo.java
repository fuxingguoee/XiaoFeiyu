package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;

public class PhoneInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;
	private String desc;
	private String isLastStep;// true 是 false s否
	private String object;// 图片验证码url
	private String picCode;

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

	public String getIsLastStep() {
		return isLastStep;
	}

	public void setIsLastStep(String isLastStep) {
		this.isLastStep = isLastStep;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getPicCode() {
		return picCode;
	}

	public void setPicCode(String picCode) {
		this.picCode = picCode;
	}

}
