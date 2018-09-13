package com.il360.xiaofeiyu.model.home;



import java.io.Serializable;
import java.util.List;

public class ArrayOfAdvert implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalCount;
	private List<Advert> list;

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<Advert> getList() {
		return list;
	}

	public void setList(List<Advert> list) {
		this.list = list;
	}
}
