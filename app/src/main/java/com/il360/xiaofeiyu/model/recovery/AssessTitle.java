package com.il360.xiaofeiyu.model.recovery;

import java.io.Serializable;
import java.util.List;

public class AssessTitle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer type;
	private Integer itemId;
	private String itemDesc;
	private List<AssessDetails> list;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public List<AssessDetails> getList() {
		return list;
	}

	public void setList(List<AssessDetails> list) {
		this.list = list;
	}

}
