package com.il360.xiaofeiyu.model.user;

import java.io.Serializable;
import java.util.List;

public class ArrayOfCommisGet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private List<UserReward> list;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<UserReward> getList() {
		return list;
	}

	public void setList(List<UserReward> list) {
		this.list = list;
	}
}
