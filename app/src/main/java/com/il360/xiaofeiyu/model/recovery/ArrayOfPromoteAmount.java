package com.il360.xiaofeiyu.model.recovery;



import java.io.Serializable;
import java.util.List;

public class ArrayOfPromoteAmount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalCount;
	private List<PromoteAmount> list;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<PromoteAmount> getList() {
		return list;
	}

	public void setList(List<PromoteAmount> list) {
		this.list = list;
	}
}
