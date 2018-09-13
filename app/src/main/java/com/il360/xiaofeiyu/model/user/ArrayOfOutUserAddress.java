package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.util.List;

public class ArrayOfOutUserAddress implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private List<OutUserAddress> list;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<OutUserAddress> getList() {
		return list;
	}

	public void setList(List<OutUserAddress> list) {
		this.list = list;
	}
}
