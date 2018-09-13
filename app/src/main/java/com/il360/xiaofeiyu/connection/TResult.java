package com.il360.xiaofeiyu.connection;

@SuppressWarnings("hiding")
public class TResult<Boolean, String> {

	private Boolean success;
	private String result;

	public TResult(Boolean success, String result) {
		super();
		this.success = success;
		this.result = result;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	

}
