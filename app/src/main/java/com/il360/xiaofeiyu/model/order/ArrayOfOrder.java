package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.util.List;

public class ArrayOfOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalCount;
	private List<Order> list;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<Order> getList() {
		return list;
	}

	public void setList(List<Order> list) {
		this.list = list;
	}
}
