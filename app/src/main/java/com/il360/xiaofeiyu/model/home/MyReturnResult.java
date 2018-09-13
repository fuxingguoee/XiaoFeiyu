package com.il360.xiaofeiyu.model.home;

import java.io.Serializable;

public class MyReturnResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer returnCode;
	private String returnMessage;
	private Object returnResult;

	public Integer getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public Object getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(Object returnResult) {
		this.returnResult = returnResult;
	}
}
