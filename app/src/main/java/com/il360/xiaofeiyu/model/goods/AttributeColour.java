package com.il360.xiaofeiyu.model.goods;

import java.io.Serializable;

public class AttributeColour implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer colourId;
	private Integer proId;
	private String colourDesc;
	private Integer status;
	private Integer flag;//非空并且为0时无货

	public Integer getColourId() {
		return colourId;
	}

	public void setColourId(Integer colourId) {
		this.colourId = colourId;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getColourDesc() {
		return colourDesc;
	}

	public void setColourDesc(String colourDesc) {
		this.colourDesc = colourDesc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
