package com.il360.xiaofeiyu.model.order;

import java.io.Serializable;
import java.util.List;

public class ArrayOfOrderPeriodsExt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalCount;
	private List<OrderPeriodsExt> list;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<OrderPeriodsExt> getList() {
		return list;
	}

	public void setList(List<OrderPeriodsExt> list) {
		this.list = list;
	}
}
