package com.il360.xiaofeiyu.model.hua;

import java.io.Serializable;
import java.util.List;

public class ArrayOfCardConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private List<CardConfig> list;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<CardConfig> getList() {
		return list;
	}

	public void setList(List<CardConfig> list) {
		this.list = list;
	}

}
