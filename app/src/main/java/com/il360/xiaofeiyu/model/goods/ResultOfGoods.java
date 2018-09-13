package com.il360.xiaofeiyu.model.goods;

import java.io.Serializable;

public class ResultOfGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String desc;
	private Goods result;
	
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
	public Goods getResult() {
		return result;
	}
	public void setResult(Goods result) {
		this.result = result;
	}
	
	
	
}
