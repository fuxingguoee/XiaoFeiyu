package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArrayOfTbOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String monthId;
	private List<Orders> orderList = new ArrayList<Orders>();

	public String getMonthId() {
		return monthId;
	}

	public void setMonthId(String monthId) {
		this.monthId = monthId;
	}

	public List<Orders> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Orders> orderList) {
		this.orderList = orderList;
	}

}
