package com.il360.xiaofeiyu.model.hua;

import java.io.Serializable;
import java.util.List;

public class ArrayOfUserAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private List<UserAccount> list;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<UserAccount> getList() {
		return list;
	}

	public void setList(List<UserAccount> list) {
		this.list = list;
	}

}
