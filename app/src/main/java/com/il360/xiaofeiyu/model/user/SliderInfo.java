package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;

public class SliderInfo implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String bigImage;
	private String blockImage;
	private String code;
	private String desc;
	private String captchaParam;


	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
	}

	public String getBlockImage() {
		return blockImage;
	}

	public void setBlockImage(String blockImage) {
		this.blockImage = blockImage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}



}
