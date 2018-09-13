package com.il360.xiaofeiyu.model.goods;

import java.io.Serializable;

public class AttributeType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer typeId;
	private Integer proId;
	private String typeDesc;
	private Integer status;
	private Integer flag;//非空并且为0时无货

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
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
