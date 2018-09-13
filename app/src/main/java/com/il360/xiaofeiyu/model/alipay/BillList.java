package com.il360.xiaofeiyu.model.alipay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BillList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String monthId;
	private List<BillItem> billList = new ArrayList<BillItem>();

	public String getMonthId() {
		return monthId;
	}

	public void setMonthId(String monthId) {
		this.monthId = monthId;
	}

	public List<BillItem> getBillList() {
		return billList;
	}

	public void setBillList(List<BillItem> billList) {
		this.billList = billList;
	}

}
