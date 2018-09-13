package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;

public class ZmxySign implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String params;
	private String sign;
	private String appId;

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
