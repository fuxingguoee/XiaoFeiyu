package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;

public class TuthenticationChannel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id **/
	private Integer tuthenticationId;
	/** 1支付宝2淘宝3京东 **/
	private Integer tuthenticationType;
	/** 1自主2魔蝎 **/
	private Integer tuthenticationMethod;
	/** updateTime **/
	private String updateTime;
	/** updator **/
	private String updator;

	public Integer getTuthenticationId() {
		return tuthenticationId;
	}

	public void setTuthenticationId(Integer tuthenticationId) {
		this.tuthenticationId = tuthenticationId;
	}

	public Integer getTuthenticationType() {
		return tuthenticationType;
	}

	public void setTuthenticationType(Integer tuthenticationType) {
		this.tuthenticationType = tuthenticationType;
	}

	public Integer getTuthenticationMethod() {
		return tuthenticationMethod;
	}

	public void setTuthenticationMethod(Integer tuthenticationMethod) {
		this.tuthenticationMethod = tuthenticationMethod;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

}
