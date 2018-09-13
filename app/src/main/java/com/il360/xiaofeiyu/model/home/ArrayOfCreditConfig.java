package com.il360.xiaofeiyu.model.home;



import java.io.Serializable;
import java.util.List;

public class ArrayOfCreditConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int  code;
	private String desc;
	private List<CreditConfig> result;
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
	public List<CreditConfig> getResult() {
		return result;
	}
	public void setResult(List<CreditConfig> result) {
		this.result = result;
	}
}
