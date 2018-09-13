package com.il360.xiaofeiyu.model.tb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaoBaoInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BasicInfo basicInfo;
	private TbAlipayInfo tbAlipayInfo;
	private List<Address> addressList = new ArrayList<Address>();
	private List<ArrayOfTbOrder> monthOrderList = new ArrayList<ArrayOfTbOrder>();

	public BasicInfo getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfo basicInfo) {
		this.basicInfo = basicInfo;
	}

	public TbAlipayInfo getTbAlipayInfo() {
		return tbAlipayInfo;
	}

	public void setTbAlipayInfo(TbAlipayInfo tbAlipayInfo) {
		this.tbAlipayInfo = tbAlipayInfo;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public List<ArrayOfTbOrder> getMonthOrderList() {
		return monthOrderList;
	}

	public void setMonthOrderList(List<ArrayOfTbOrder> monthOrderList) {
		this.monthOrderList = monthOrderList;
	}

}
