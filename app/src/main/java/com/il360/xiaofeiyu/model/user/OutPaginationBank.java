package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.util.List;

public class OutPaginationBank implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private List<Bank> list;

	public List<Bank> getList() {
		return list;
	}

	public void setList(List<Bank> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
